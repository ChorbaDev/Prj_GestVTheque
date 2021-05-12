package Modele;

import javafx.beans.property.*;

public class Client {
    private IntegerProperty idClient;
    private StringProperty nomClient;
    private StringProperty prenomClient;
    private StringProperty mailClient;
    private BooleanProperty clientFidele;

    public Client(int idClient, String nomClient, String prenomClient, String mailClient, Boolean clientFidele) {
        this.idClient =new SimpleIntegerProperty(idClient);
        this.nomClient =new SimpleStringProperty(nomClient) ;
        this.prenomClient = new SimpleStringProperty(prenomClient);
        this.mailClient =new SimpleStringProperty(mailClient) ;
        this.clientFidele =new SimpleBooleanProperty(clientFidele) ;
    }

    public Client(StringProperty nomClient, StringProperty prenomClient, StringProperty mailClient, BooleanProperty clientFidele) {
        this.nomClient = nomClient;
        this.prenomClient = prenomClient;
        this.mailClient = mailClient;
        this.clientFidele = clientFidele;
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

    public String getNomClient() {
        return nomClient.get();
    }

    public StringProperty nomClientProperty() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient.set(nomClient);
    }

    public String getPrenomClient() {
        return prenomClient.get();
    }

    public StringProperty prenomClientProperty() {
        return prenomClient;
    }

    public void setPrenomClient(String prenomClient) {
        this.prenomClient.set(prenomClient);
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
}
