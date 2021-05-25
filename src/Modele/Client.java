package Modele;

import javafx.beans.property.*;

public class Client extends Personne {
    private IntegerProperty idClient;
    private StringProperty mailClient;
    private BooleanProperty clientFidele;

    public Client(int idClient, String nomClient, String prenomClient, String mailClient, Boolean clientFidele) {
        super(nomClient, prenomClient);
        this.idClient = new SimpleIntegerProperty(idClient);
        this.mailClient = new SimpleStringProperty(mailClient);
        this.clientFidele = new SimpleBooleanProperty(clientFidele);
    }

    public Client(String nomClient, String prenomClient, String mailClient, Boolean clientFidele) {
        super(nomClient, prenomClient);
        this.mailClient = new SimpleStringProperty(mailClient);
        this.clientFidele = new SimpleBooleanProperty(clientFidele);
    }

    public int getIdClient() {
        return idClient.get();
    }

    public void setIdClient(int idClient) {
        this.idClient.set(idClient);
    }

    public IntegerProperty idClientProperty() {
        return idClient;
    }

    public String getMailClient() {
        return mailClient.get();
    }

    public void setMailClient(String mailClient) {
        this.mailClient.set(mailClient);
    }

    public StringProperty mailClientProperty() {
        return mailClient;
    }

    public boolean isClientFidele() {
        return clientFidele.get();
    }

    public void setClientFidele(boolean clientFidele) {
        this.clientFidele.set(clientFidele);
    }

    public BooleanProperty clientFideleProperty() {
        return clientFidele;
    }

    public boolean equals(Client cl) {
        return (cl.nom.getValue().equals(nom.getValue())) && (cl.prenom.getValue().equals(prenom.getValue())) && (cl.mailClient.getValue().equals(mailClient.getValue())) && (cl.idClient.getValue().equals(idClient.getValue())) && (cl.isClientFidele() == this.isClientFidele());
    }

    @Override
    public String toString()
    {
        return "Client{" +
                "idClient=" + idClient +
                ", mailClient=" + mailClient +
                ", clientFidele=" + clientFidele +
                '}';
    }
}