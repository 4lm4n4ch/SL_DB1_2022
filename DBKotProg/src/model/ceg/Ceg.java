package model.ceg;

public class Ceg {
    private int ceg_id;
    private String ceg_nev;
    private String kapcsolattarto_nev;
    private String kapcsolatttarto_email;
    private String jelszo;
    private String telefonszam;

    public Ceg(int ceg_id, String ceg_nev, String kapcsolattarto_nev, String kapcsolatttarto_email, String jelszo, String telefonszam) {
        this.ceg_id = ceg_id;
        this.ceg_nev = ceg_nev;
        this.kapcsolattarto_nev = kapcsolattarto_nev;
        this.kapcsolatttarto_email = kapcsolatttarto_email;
        this.jelszo = jelszo;
        this.telefonszam = telefonszam;
    }


    public int getCeg_id() {
        return ceg_id;
    }

    public void setCeg_id(int ceg_id) {
        this.ceg_id = ceg_id;
    }

    public String getCeg_nev() {
        return ceg_nev;
    }

    public void setCeg_nev(String ceg_nev) {
        this.ceg_nev = ceg_nev;
    }

    public String getKapcsolattarto_nev() {
        return kapcsolattarto_nev;
    }

    public void setKapcsolattarto_nev(String kapcsolattarto_nev) {
        this.kapcsolattarto_nev = kapcsolattarto_nev;
    }

    public String getKapcsolatttarto_email() {
        return kapcsolatttarto_email;
    }

    public void setKapcsolatttarto_email(String kapcsolatttarto_email) {
        this.kapcsolatttarto_email = kapcsolatttarto_email;
    }

    public String getJelszo() {
        return jelszo;
    }

    public void setJelszo(String jelszo) {
        this.jelszo = jelszo;
    }

    public String getTelefonszam() {
        return telefonszam;
    }

    public void setTelefonszam(String telefonszam) {
        this.telefonszam = telefonszam;
    }
}
