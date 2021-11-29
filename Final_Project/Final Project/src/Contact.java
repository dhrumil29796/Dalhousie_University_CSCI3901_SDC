import java.time.LocalDate;

// Public class Contact
// Each instance of this class stores an individual, date & duration for each contact that occurs
public class Contact {

    // Global variables to store each contact
    String individual;
    LocalDate date;
    int duration;

    // A default constructor to instantiate Contact class
    public Contact() {

    }

    // Parameterized constructor to instantiate Contact class
    // This constructor sets values for individual, date & duration of contact
    public Contact(String individual, LocalDate date, int duration) {
        this.individual = individual;
        this.date = date;
        this.duration = duration;
    }

    // Get method for individual
    public String getIndividual() {
        return individual;
    }

    // Get method for date
    public LocalDate getDate() {
        return date;
    }

    // Get method for duration
    public int getDuration() {
        return duration;
    }

    // Set method for individual
    public void setIndividual(String individual) {
        this.individual = individual;
    }

    // Set method for date
    public void setDate(LocalDate date) {
        this.date = date;
    }

    // Set method for duration
    public void setDuration(int duration) {
        this.duration = duration;
    }
}
