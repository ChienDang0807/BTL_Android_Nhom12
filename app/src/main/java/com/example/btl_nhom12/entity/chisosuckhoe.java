package com.example.btl_nhom12.entity;

public class chisosuckhoe {
        private String sdt;       // Khóa ngoại liên kết với User
        private String date;      // Ngày đo
        private String weight;    // Cân nặng
        private String height;    // Chiều cao
        private String bmi;       // BMI

        // Constructor
        public chisosuckhoe(String sdt, String date, String weight, String height, String bmi) {


            this.sdt = sdt;
            this.date = date;
            this.weight = weight;
            this.height = height;
            this.bmi = bmi;
        }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getBmi() {
        return bmi;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "chisosuckhoe{" +
                "sdt='" + sdt + '\'' +
                ", date='" + date + '\'' +
                ", weight='" + weight + '\'' +
                ", height='" + height + '\'' +
                ", bmi='" + bmi + '\'' +
                '}';
    }
}
