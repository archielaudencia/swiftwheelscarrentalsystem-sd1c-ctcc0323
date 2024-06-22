import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import java.util.HashMap;
import java.util.Map;

public class CarRentalSystemAWT extends Frame {
    private final Choice carSelect;
    private final Choice durationSelect;
    private final DatePanel pickupDatePanel;
    private final DatePanel returnDatePanel;
    private final Label priceDisplay;
    private final Label resultDisplay;

    private final Map<String, CarDetails> cars = new HashMap<>();

    public CarRentalSystemAWT() {
        cars.put("Toyota Vios 1.3 XE CVT (2024) 5 seater", new CarDetails(1000, "The Toyota Vios is a compact sedan known for its reliability and fuel efficiency."));
        cars.put("2022 Chevrolet Tahoe RST (fifth generation) 7-9 seater", new CarDetails(3000, "The Chevrolet Tahoe RST is a full-size SUV with spacious seating and powerful performance."));
        cars.put("Suzuki Jimny Sierra (2018) 4 seaters", new CarDetails(3000, "The Suzuki Jimny Sierra is a rugged and compact SUV, perfect for off-road adventures."));
        cars.put("2019 Mitsubishi Xpander Mitsubishi Xpander Ultimate Nissan Livina", new CarDetails(3000, "The Mitsubishi Xpander is a versatile MPV with ample space and comfort for passengers."));
        cars.put("Toyota Hiace Commuter 3.0 MT (2018) 15 Seater", new CarDetails(10000, "The Toyota Hiace Commuter is a spacious van ideal for large groups and transport services."));

        setTitle("Car Rental System");
        setSize(800, 600);
        setLayout(new BorderLayout());
        setBackground(new Color(213, 211, 207));

        Panel headerPanel = new Panel();
        headerPanel.setLayout(new GridLayout(2, 1));
        headerPanel.setBackground(new Color(64, 64, 64));
        headerPanel.setForeground(Color.WHITE);
        headerPanel.add(createLabel("SwiftWheels Car Rental", 24, Label.CENTER));
        headerPanel.add(createLabel("Your Gateway to Effortless Car Rentals and Memorable Drives", 16, Label.CENTER));
        add(headerPanel, BorderLayout.NORTH);

        Panel centerPanel = new Panel();
        centerPanel.setLayout(new GridLayout(12, 1, 10, 10));
        centerPanel.setBackground(new Color(213, 211, 207));

        centerPanel.add(createLabel("Select the car you want to rent:", 14, Label.LEFT));
        carSelect = new Choice();
        for (String car : cars.keySet()) {
            carSelect.add(car);
        }
        centerPanel.add(carSelect);

        centerPanel.add(createLabel("How long would you like to rent the car for?", 14, Label.LEFT));
        durationSelect = new Choice();
        durationSelect.add("1 Day");
        durationSelect.add("2 Days");
        durationSelect.add("3 Days");
        durationSelect.add("4 Days");
        durationSelect.add("5 Days");
        durationSelect.add("6 Days");
        durationSelect.add("1 Week");
        durationSelect.add("1 Month");
        centerPanel.add(durationSelect);

        centerPanel.add(createLabel("Pickup Date:", 14, Label.LEFT));
        pickupDatePanel = new DatePanel();
        centerPanel.add(pickupDatePanel);

        centerPanel.add(createLabel("Return Date:", 14, Label.LEFT));
        returnDatePanel = new DatePanel();
        centerPanel.add(returnDatePanel);

        priceDisplay = new Label("Price: ");
        centerPanel.add(priceDisplay);

        centerPanel.add(createLabel("Please enter your cash:", 14, Label.LEFT));
        TextField cashInput = new TextField(10);
        centerPanel.add(cashInput);

        resultDisplay = new Label("Result: ");
        centerPanel.add(resultDisplay);

        add(centerPanel, BorderLayout.CENTER);

        Panel buttonPanel = new Panel();
        buttonPanel.setLayout(new FlowLayout());
        Button rentButton = new Button("Rent Car");
        rentButton.addActionListener(e -> rentCar(cashInput.getText()));
        buttonPanel.add(rentButton);
        add(buttonPanel, BorderLayout.SOUTH);

        carSelect.addItemListener(e -> updatePrice());
        durationSelect.addItemListener(e -> updatePrice());
        pickupDatePanel.addDateChangeListener(e -> updatePrice());
        returnDatePanel.addDateChangeListener(e -> updatePrice());
        updatePrice();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });

        showWelcomeWindow();
    }

    private void showWelcomeWindow() {
        Dialog welcomeDialog = new Dialog(this, "Welcome", true);
        welcomeDialog.setLayout(new BorderLayout());
        Label welcomeLabel = new Label("Welcome to SwiftWheels Car Rental System!", Label.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        welcomeDialog.add(welcomeLabel, BorderLayout.CENTER);

        Panel buttonPanel = new Panel();
        buttonPanel.setLayout(new FlowLayout());
        Button okButton = new Button("Proceed to Car Rental");
        okButton.addActionListener(e -> welcomeDialog.dispose());
        buttonPanel.add(okButton);

        welcomeDialog.add(buttonPanel, BorderLayout.SOUTH);
        welcomeDialog.setSize(400, 200);
        welcomeDialog.setLocationRelativeTo(this);
        welcomeDialog.setVisible(true);
    }

    private Label createLabel(String text, int fontSize, int alignment) {
        Label label = new Label(text, alignment);
        label.setFont(new Font("SansSerif", Font.PLAIN, fontSize));
        return label;
    }

    private void updatePrice() {
        String selectedCar = carSelect.getSelectedItem();
        String durationText = durationSelect.getSelectedItem();
        int rentDuration = durationText.equals("1 Day") ? 1 : durationText.equals("1 Week") ? 7 : 30;

        Date pickupDate = pickupDatePanel.getSelectedDate();
        Date returnDate = returnDatePanel.getSelectedDate();

        if (pickupDate != null && returnDate != null) {
            long diffInMillis = returnDate.getTime() - pickupDate.getTime();
            int daysDifference = (int) (diffInMillis / (1000 * 60 * 60 * 24)) + 1;
            int rentPrice = cars.get(selectedCar).getPrice() * daysDifference * rentDuration;
            String carDescription = cars.get(selectedCar).getDescription();
            priceDisplay.setText("Price for renting the " + selectedCar + " for " + daysDifference + " days: PHP " + rentPrice + "\nDescription: " + carDescription);
        } else {
            priceDisplay.setText("Price: ");
        }
    }

    private void rentCar(String cashInputText) {
        String selectedCar = carSelect.getSelectedItem();
        String durationText = durationSelect.getSelectedItem();

        Date pickupDate = pickupDatePanel.getSelectedDate();
        Date returnDate = returnDatePanel.getSelectedDate();

        int rentDuration = durationText.equals("1 Day") ? 1 : durationText.equals("1 Week") ? 7 : 30;
        double userCash;
        try {
            userCash = Double.parseDouble(cashInputText);
        } catch (NumberFormatException e) {
            resultDisplay.setText("Result: Error - Invalid cash amount.");
            return;
        }

        if (pickupDate != null && returnDate != null) {
            long diffInMillis = returnDate.getTime() - pickupDate.getTime();
            int daysDifference = (int) (diffInMillis / (1000 * 60 * 60 * 24)) + 1;
            int rentPrice = cars.get(selectedCar).getPrice() * daysDifference * rentDuration;
            double change = userCash - rentPrice;

            if (change >= 0) {
                showUserInfoDialog(selectedCar, rentPrice, daysDifference, userCash, change, pickupDate, returnDate);
            } else {
                resultDisplay.setText("Result: Error - Insufficient Balance.");
            }
        } else {
            resultDisplay.setText("Result: Error - Please select valid pickup and return dates.");
        }
    }

    private void showUserInfoDialog(String car, int price, int days, double cash, double change, Date pickupDate, Date returnDate) {
        Dialog userInfoDialog = new Dialog(this, "User Information", true);
        userInfoDialog.setLayout(new BorderLayout());

        Panel userInfoPanel = new Panel();
        userInfoPanel.setLayout(new GridLayout(8, 2, 10, 10));

        userInfoPanel.add(createLabel("Name:", 14, Label.RIGHT));
        TextField nameInput = new TextField(10);
        userInfoPanel.add(nameInput);

        userInfoPanel.add(createLabel("Contact Number:", 14, Label.RIGHT));
        TextField contactInput = new TextField(10);
        userInfoPanel.add(contactInput);

        userInfoPanel.add(createLabel("Email:", 14, Label.RIGHT));
        TextField emailInput = new TextField(10);
        userInfoPanel.add(emailInput);

        userInfoPanel.add(createLabel("Address:", 14, Label.RIGHT));
        TextField addressInput = new TextField(10);
        userInfoPanel.add(addressInput);

        userInfoPanel.add(createLabel("Pickup Date:", 14, Label.RIGHT));
        Label pickupDateLabel = new Label(pickupDate.toString());
        userInfoPanel.add(pickupDateLabel);

        userInfoPanel.add(createLabel("Return Date:", 14, Label.RIGHT));
        Label returnDateLabel = new Label(returnDate.toString());
        userInfoPanel.add(returnDateLabel);

        Panel termsPanel = new Panel(new BorderLayout());
        TextArea termsTextArea = new TextArea();
        termsTextArea.setEditable(false);
        termsTextArea.setText("""
            Car Rental Terms and Conditions

            1. Agreement to Terms

            By submitting this form, you agree to abide by the following terms and conditions ("Terms") governing the rental of a vehicle from SwiftWheels.

            2. Rental Period

            The rental period begins at the specified pickup time and date and ends upon the vehicle's return to SwiftWheels at the agreed-upon time. Any extension of the rental period must be approved by SwiftWheels and may be subject to additional fees.

            3. Rental Fees and Payment

            Rental charges, including any applicable taxes, fees, and optional services, must be paid in full before or upon vehicle pickup. Payments may be made via accepted payment methods.

            4. Driver Requirements

            All drivers must possess a valid driver's license and meet SwiftWheels' minimum age requirements. Additional drivers may be added to the rental agreement, subject to approval and additional fees.

            5. Vehicle Use and Return

            The vehicle must be returned to SwiftWheels in the same condition as received, with all equipment and accessories intact. Any damage or excessive wear may result in additional charges.

            6. Liability and Insurance

            Renters are responsible for any damage to the vehicle and for obtaining adequate insurance coverage. SwiftWheels may offer optional insurance coverage for an additional fee.

            7. Termination

            SwiftWheels reserves the right to terminate the rental agreement and reclaim the vehicle at any time if the renter fails to comply with the Terms or misuses the vehicle.

            By checking the box below, you acknowledge that you have read and agree to these Terms and authorize SwiftWheels to charge the payment method provided for any charges related to the rental agreement.
            """);

        termsPanel.add(termsTextArea, BorderLayout.CENTER);

        Panel dataPrivacyPanel = new Panel(new BorderLayout());
        TextArea dataPrivacyTextArea = new TextArea();
        dataPrivacyTextArea.setEditable(false);
        dataPrivacyTextArea.setText("""
            SwiftWheels Car Rental Data Privacy Policy

            Introduction

            SwiftWheels Car Rental (“we”, “our”, “us”) is committed to protecting and respecting your privacy. This Data Privacy Policy explains how we collect, use, and protect your personal data when you use our car rental services.

            Information We Collect

            We may collect and process the following data about you:

                • Personal Identification Information: Name, address, email address, phone number, date of birth, and driver’s license details.
                • Payment Information: Credit/debit card details, billing address, and other financial information.
                • Rental Information: Details of the vehicles rented, rental period, pickup and drop-off locations, and other rental preferences.
                • Usage Data: Information on how you use our website, mobile app, and services, including IP address, browser type, pages visited, and interaction data.
                • Location Data: Data regarding your current and past locations when using our rental services, if you opt-in to this service.

            How We Use Your Information

            We use your information to:

                • Provide and manage your car rental bookings.
                • Process payments and manage billing.
                • Communicate with you about your bookings, services, and offers.
                • Improve our services and customer experience.
                • Comply with legal obligations and protect our rights and interests.

            Sharing Your Information

            We do not sell, trade, or otherwise transfer your personal data to outside parties except:

                • Service Providers: Trusted third-party companies that assist us in operating our services, conducting our business, or serving our users, as long as those parties agree to keep this information confidential.
                • Legal Compliance: When we are required to comply with legal obligations, regulations, or enforceable governmental requests.
                • Business Transfers: In the event of a merger, acquisition, or sale of assets, your personal data may be transferred to the acquiring entity.

            Data Security

            We implement appropriate technical and organizational measures to protect your personal data against unauthorized access, alteration, disclosure, or destruction. These measures include:

                • Secure server and data storage.
                • Encryption of sensitive information.
                • Regular security assessments and audits.

            Your Rights

            You have the right to:
                • Access your personal data and receive a copy of the information we hold about you.
                • Rectify any inaccuracies in your personal data.
                • Request the deletion of your personal data, subject to legal and contractual restrictions.
                • Object to or restrict the processing of your personal data.
                • Withdraw your consent to the processing of your personal data at any time, where consent is the legal basis for processing.

            Cookies and Tracking Technologies

            We use cookies and similar tracking technologies to enhance your experience on our website and mobile app. You can control the use of cookies through your browser settings.

            Changes to This Privacy Policy

            We may update this Data Privacy Policy from time to time. Any changes will be posted on this page, and we will notify you of significant changes through our website or by other means.

            Contact Us
            If you have any questions or concerns about this Data Privacy Policy or our data practices, please contact us at:

            Email: privacy@swiftwheels.com
                • Phone: +1-800-123-4567
                • Address: SwiftWheels Car Rental, 123 Villa Lina St. , Balanga City, Bataan
            """);

        dataPrivacyPanel.add(dataPrivacyTextArea, BorderLayout.CENTER);

        Panel buttonPanel = new Panel(new FlowLayout(FlowLayout.CENTER));

        // Panel to hold checkboxes
        Panel checkboxPanel = new Panel(new GridLayout(2, 1));
        Checkbox agreeCheckbox = new Checkbox("I agree to the terms and conditions");
        Checkbox agreeDataPrivacyCheckbox = new Checkbox("I agree to the data privacy policy");

        checkboxPanel.add(agreeCheckbox);
        checkboxPanel.add(agreeDataPrivacyCheckbox);

        userInfoPanel.add(checkboxPanel);


        Button reserveButton = new Button("Reserve Car");
        reserveButton.addActionListener(e -> {
            String name = nameInput.getText();
            String contact = contactInput.getText();
            String email = emailInput.getText();
            String address = addressInput.getText();

            showReceipt(name, contact, email, address, car, price, days, cash, change);
            userInfoDialog.setVisible(false);
            userInfoDialog.dispose();
        });
        userInfoPanel.add(reserveButton);

        userInfoDialog.add(userInfoPanel, BorderLayout.CENTER);

        Panel combinedPanel = new Panel(new GridLayout(1, 2));

        Panel termsAndPrivacyPanel = new Panel(new BorderLayout());
        termsAndPrivacyPanel.add(termsPanel, BorderLayout.NORTH);
        termsAndPrivacyPanel.add(dataPrivacyPanel, BorderLayout.SOUTH);

        combinedPanel.add(termsAndPrivacyPanel);

        userInfoDialog.add(combinedPanel, BorderLayout.EAST);

        userInfoDialog.pack();
        userInfoDialog.setLocationRelativeTo(this);
        userInfoDialog.setVisible(true);
    }

    private void showReceipt(String name, String contact, String email, String address, String car, int price, int days, double cash, double change) {
        Dialog receiptDialog = new Dialog(this, "Receipt", true);
        receiptDialog.setLayout(new BorderLayout());

        Panel receiptPanel = new Panel();
        receiptPanel.setLayout(new GridLayout(6, 2, 10, 10));

        receiptPanel.add(createLabel("Name:", 14, Label.RIGHT));
        receiptPanel.add(new Label(name));

        receiptPanel.add(createLabel("Contact Number:", 14, Label.RIGHT));
        receiptPanel.add(new Label(contact));

        receiptPanel.add(createLabel("Email:", 14, Label.RIGHT));
        receiptPanel.add(new Label(email));

        receiptPanel.add(createLabel("Address:", 14, Label.RIGHT));
        receiptPanel.add(new Label(address));

        receiptPanel.add(createLabel("Car Rented:", 14, Label.RIGHT));
        receiptPanel.add(new Label(car));

        receiptPanel.add(createLabel("Rent Price:", 14, Label.RIGHT));
        receiptPanel.add(new Label("PHP " + price));

        receiptPanel.add(createLabel("Cash Paid:", 14, Label.RIGHT));
        receiptPanel.add(new Label("PHP " + cash));

        receiptPanel.add(createLabel("Change:", 14, Label.RIGHT));
        receiptPanel.add(new Label("PHP " + change));

        Button closeButton = new Button("Close");
        closeButton.addActionListener(e -> receiptDialog.dispose());
        Panel buttonPanel = new Panel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(closeButton);

        receiptDialog.add(receiptPanel, BorderLayout.CENTER);
        receiptDialog.add(buttonPanel, BorderLayout.SOUTH);
        receiptDialog.pack();
        receiptDialog.setLocationRelativeTo(this);
        receiptDialog.setVisible(true);
    }

    public static void main(String[] args) {
        CarRentalSystemAWT carRentalSystemAWT = new CarRentalSystemAWT();
        carRentalSystemAWT.setVisible(true);
    }
}

class DatePanel extends Panel {
    private final Choice monthChoice;
    private final Label dayLabel;
    private final Choice yearChoice;
    private final Button dayUpButton;
    private final Button dayDownButton;

    private int day;
    private int month;
    private int year;

    public DatePanel() {
        setLayout(new FlowLayout());

        dayUpButton = new Button("▲");
        dayDownButton = new Button("▼");
        dayLabel = new Label();

        monthChoice = new Choice();
        for (int i = 0; i < 12; i++) {
            monthChoice.add(new DateFormatSymbols().getMonths()[i]);
        }

        yearChoice = new Choice();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int y = currentYear; y <= currentYear + 5; y++) {
            yearChoice.add(String.valueOf(y));
        }

        dayUpButton.addActionListener(e -> updateDay(1));
        dayDownButton.addActionListener(e -> updateDay(-1));
        monthChoice.addItemListener(e -> updateDate());
        yearChoice.addItemListener(e -> updateDate());

        add(dayUpButton);
        add(dayLabel);
        add(dayDownButton);
        add(monthChoice);
        add(yearChoice);

        year = currentYear;
        month = 0;
        day = 1;
        updateDate();
    }

    private void updateDay(int increment) {
        int daysInMonth = getDaysInMonth(year, month);
        day += increment;
        if (day > daysInMonth) {
            day = 1;
        } else if (day < 1) {
            day = daysInMonth;
        }
        dayLabel.setText(String.valueOf(day));
    }

    private void updateDate() {
        month = monthChoice.getSelectedIndex();
        year = Integer.parseInt(yearChoice.getSelectedItem());
        int daysInMonth = getDaysInMonth(year, month);
        if (day > daysInMonth) {
            day = daysInMonth;
        }
        dayLabel.setText(String.valueOf(day));
    }

    private int getDaysInMonth(int year, int month) {
        Calendar cal = new GregorianCalendar(year, month, 1);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public Date getSelectedDate() {
        Calendar cal = new GregorianCalendar(year, month, day);
        return cal.getTime();
    }

    public void addDateChangeListener(DateChangeListener listener) {
        dayUpButton.addActionListener(e -> listener.dateChanged(new DateChangeEvent(this, getSelectedDate())));
        dayDownButton.addActionListener(e -> listener.dateChanged(new DateChangeEvent(this, getSelectedDate())));
        monthChoice.addItemListener(e -> listener.dateChanged(new DateChangeEvent(this, getSelectedDate())));
        yearChoice.addItemListener(e -> listener.dateChanged(new DateChangeEvent(this, getSelectedDate())));
    }
}

interface DateChangeListener {
    void dateChanged(DateChangeEvent event);
}

class DateChangeEvent extends AWTEvent {
    private final DatePanel source;
    private final Date date;

    public DateChangeEvent(DatePanel source, Date date) {
        super(source, AWTEvent.RESERVED_ID_MAX + 1);
        this.source = source;
        this.date = date;
    }

    public DatePanel getSource() {
        return source;
    }

    public Date getDate() {
        return date;
    }
}

class CarDetails {
    private final int price;
    private final String description;

    public CarDetails(int price, String description) {
        this.price = price;
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }
}
