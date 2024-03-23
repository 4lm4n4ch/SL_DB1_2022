package model.allaskerso;

public class Allaskereso {

    private String nev;
    private String jelszo;
    private String email;
    private String telefonszam;
    private String iskolaiVegzetseg;

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public String getJelszo() {
        return jelszo;
    }

    public void setJelszo(String jelszo) {
        this.jelszo = jelszo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefonszam() {
        return telefonszam;
    }

    public void setTelefonszam(String telefonszam) {
        this.telefonszam = telefonszam;
    }

    public String getIskolaiVegzetseg() {
        return iskolaiVegzetseg;
    }

    public void setIskolaiVegzetseg(String iskolaiVegzetseg) {
        this.iskolaiVegzetseg = iskolaiVegzetseg;
    }

    public Allaskereso(String email,String nev, String jelszo,  String telefonszam) {
        this.nev = nev;
        this.jelszo = jelszo;
        this.email = email;
        this.telefonszam = telefonszam;
    }

    public Allaskereso() {
    }
}
