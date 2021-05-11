package Modele;

public class Client {
    private String idClient;
    private String nomClient;
    private String prenomClient;
    private boolean clientFidele;

    public Client(String idClient, String nomClient, String prenomClient, boolean clientFidele) {
        this.idClient = idClient;
        this.nomClient = nomClient;
        this.prenomClient = prenomClient;
        this.clientFidele = clientFidele;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getPrenomClient() {
        return prenomClient;
    }

    public void setPrenomClient(String prenomClient) {
        this.prenomClient = prenomClient;
    }

    public boolean isClientFidele() {
        return clientFidele;
    }

    public void setClientFidele(boolean clientFidele) {
        this.clientFidele = clientFidele;
    }


}
