package com.aniket.contacts.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.io.IOException;

public class ContactData {

    private final String filePath = "src/main/resources/contactsXMLSource";
    private static final ContactData instance = new ContactData();
    private final ObservableList<ContactItem> contacts;


    private ContactData(){
        this.contacts = FXCollections.observableArrayList();
    }

    public ObservableList<ContactItem> getContacts() {
        return contacts;
    }

    public static ContactData getInstance() {
        return instance;
    }

    public void readData() {
        try{
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(this.filePath);
            Element rootEl = doc.getDocumentElement();
            NodeList nl = rootEl.getElementsByTagName("Contact");
            for(int i=0; i<nl.getLength(); i++){
                if(nl.item(i).getNodeType() == Node.ELEMENT_NODE){
                    Element el = ((Element) nl.item(i));
                    ContactData.getInstance().contacts.add(new ContactItem(
                            el.getElementsByTagName("Name").item(0).getTextContent(),
                            el.getElementsByTagName("PhoneNo").item(0).getTextContent(),
                            el.getElementsByTagName("EmailId").item(0).getTextContent(),
                            el.getElementsByTagName("Address").item(0).getTextContent(),
                            el.getElementsByTagName("Notes").item(0).getTextContent()
                    ));
                }
            }

        }catch (ParserConfigurationException | SAXException | IOException e){
            e.printStackTrace();
        }
    }

    public void writeData() {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element rootEl = doc.createElement("Contacts");
            for(ContactItem contactItem : this.contacts) {
                Element contactEl = doc.createElement("Contact");

                Element nameEl = doc.createElement("Name");
                nameEl.setTextContent(contactItem.getName());
                contactEl.appendChild(nameEl);

                Element phoneNoEl = doc.createElement("PhoneNo");
                phoneNoEl.setTextContent(contactItem.getPhoneNo());
                contactEl.appendChild(phoneNoEl);

                Element emailEl = doc.createElement("EmailId");
                emailEl.setTextContent(contactItem.getEmail());
                contactEl.appendChild(emailEl);

                Element addressEl = doc.createElement("Address");
                addressEl.setTextContent(contactItem.getAddress());
                contactEl.appendChild(addressEl);

                Element notesEl = doc.createElement("Notes");
                notesEl.setTextContent(contactItem.getNotes());
                contactEl.appendChild(notesEl);

                rootEl.appendChild(contactEl);
            }

            doc.appendChild(rootEl);

            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                tr.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(this.filePath)));
            }catch (TransformerException | IOException  tr){
                tr.printStackTrace();
            }

        }catch (ParserConfigurationException p) {
            p.printStackTrace();
        }
    }
}
