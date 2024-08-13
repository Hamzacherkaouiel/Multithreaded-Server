package WithVirtualThread;

public class Car {
    public String brand;
    public int Matricule;
    public  Car(String B,int M){
        this.brand=B;
        this.Matricule=M;
    }
    public  Car(){

    }

    @Override
    public String toString() {
        return "Car{" +
                "brand='" + brand + '\'' +
                ", Matricule=" + Matricule +
                '}';
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getMatricule() {
        return Matricule;
    }

    public void setMatricule(int matricule) {
        Matricule = matricule;
    }
}
