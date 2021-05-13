package Modele;

import javafx.beans.property.*;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.util.Objects;

public class Client extends Personne{
    private IntegerProperty idClient;
    private StringProperty mailClient;
    private BooleanProperty clientFidele;

    public Client(int idClient, String nomClient, String prenomClient, String mailClient, Boolean clientFidele) {
        super(nomClient,prenomClient);
        this.idClient =new SimpleIntegerProperty(idClient);
        this.mailClient =new SimpleStringProperty(mailClient) ;
        this.clientFidele =new SimpleBooleanProperty(clientFidele) ;
    }

    public Client(String nomClient, String prenomClient, String mailClient, Boolean clientFidele) {
       super(nomClient,prenomClient);
        this.mailClient =new SimpleStringProperty(mailClient) ;
        this.clientFidele =new SimpleBooleanProperty(clientFidele) ;
    }

    public int getIdClient() {
        return idClient.get();
    }

    public IntegerProperty idClientProperty() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient.set(idClient);
    }

    public String getMailClient() {
        return mailClient.get();
    }

    public StringProperty mailClientProperty() {
        return mailClient;
    }

    public void setMailClient(String mailClient) {
        this.mailClient.set(mailClient);
    }

    public boolean isClientFidele() {
        return clientFidele.get();
    }

    public BooleanProperty clientFideleProperty() {
        return clientFidele;
    }

    public void setClientFidele(boolean clientFidele) {
        this.clientFidele.set(clientFidele);
    }

    public boolean equals(Client cl) {
        return (cl.nom.getValue().equals(nom.getValue())) && (cl.prenom.getValue().equals(prenom.getValue())) && (cl.mailClient.getValue().equals(mailClient.getValue())) && (cl.idClient.getValue().equals(idClient.getValue())) && (cl.isClientFidele()==this.isClientFidele()) ;
    }

}