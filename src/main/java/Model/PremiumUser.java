package Model;

import java.time.LocalDate;

public class PremiumUser extends User {
    private LocalDate subscriptionEndDate;

    public PremiumUser(String username, String password, String fullName, String email, String phone, String profileCover , double balance , LocalDate subscriptionEndDate) {
        super(username, password, fullName, email, phone, profileCover , balance);
        this.subscriptionEndDate = subscriptionEndDate;
    }

    public LocalDate getSubscriptionEndDate() {
        return subscriptionEndDate;
    }

    public void setSubscriptionEndDate(LocalDate subscriptionEndDate) {
        this.subscriptionEndDate = subscriptionEndDate;
    }
}
