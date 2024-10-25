import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToeflSpeakingTimer extends JFrame {
    private JLabel timeLabel;     // Label to display countdown
    private JLabel textLabel;     // Label to display "Preparation Time" or "Speaking Time"
    private JButton startButton;
    private JButton resetButton;
    private JComboBox<String> partSelector;
    private Timer timer;
    private int timeRemaining;  // Time in seconds
    private int prepTime;       // Preparation time in seconds
    private int speakTime;      // Speaking time in seconds
    private boolean isPreparation;
    private boolean isRunning;  // To track if the timer is running

    public ToeflSpeakingTimer() {
        createUI();
    }

    private void createUI() {
        setTitle("TOEFL Speaking Timer");
        setSize(new Dimension(500, 400));  // Increased window width
        setResizable(false);  // Disable resizing
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Use BorderLayout for the main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create a panel for the center content
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Label for instructions
        textLabel = new JLabel("Select a Part to Start", SwingConstants.CENTER);
        textLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Initial time label placeholder
        timeLabel = new JLabel(" ", SwingConstants.CENTER);
        timeLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        timeLabel.setForeground(new Color(50, 50, 50));
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timeLabel.setMaximumSize(new Dimension(250, 50));  // Fixed size to avoid layout jumping

        // Part selector for different speaking parts
        String[] parts = {"Part 1: 15s Prep, 45s Speak", "Part 2: 30s Prep, 60s Speak",
                "Part 3: 30s Prep, 60s Speak", "Part 4: 20s Prep, 60s Speak"};
        partSelector = new JComboBox<>(parts);
        partSelector.setFont(new Font("SansSerif", Font.PLAIN, 16));
        partSelector.setMaximumSize(new Dimension(250, 40));
        partSelector.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Start button setup
        startButton = new JButton("Start Timer");
        startButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        startButton.setFocusPainted(false);
        startButton.setBackground(new Color(60, 179, 113)); // Flat design
        startButton.setForeground(Color.WHITE);
        startButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Reset button setup
        resetButton = new JButton("Reset");
        resetButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        resetButton.setFocusPainted(false);
        resetButton.setBackground(new Color(220, 53, 69));
        resetButton.setForeground(Color.WHITE);
        resetButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add action listeners to buttons
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startTimer();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetTimer();
            }
        });

        // Add components to center panel (partSelector, textLabel, timeLabel, etc.)
        centerPanel.add(partSelector);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(textLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(timeLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(startButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(resetButton);

        // Add center panel to the main panel
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Create a panel for the watermark, align it to the right
        JPanel watermarkPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel watermark = new JLabel("Author: Ratatoskr");
        watermark.setFont(new Font("Monospaced", Font.ITALIC, 12));  // Changed to Monospaced font
        watermark.setForeground(new Color(150, 150, 150));
        watermarkPanel.add(watermark);

        // Add watermark panel to the bottom of the main panel
        mainPanel.add(watermarkPanel, BorderLayout.SOUTH);

        add(mainPanel);
        pack();  // Adjust window size to fit content
    }

    // Method to start the timer
    private void startTimer() {
        if (isRunning) {
            return;  // Prevent starting if timer is already running
        }

        setPartTimes();  // Set preparation and speaking times based on selected part

        isPreparation = true;
        timeRemaining = prepTime;
        textLabel.setText("Preparation Time:");
        timeLabel.setText(timeRemaining + " seconds");
        timeLabel.setForeground(new Color(50, 50, 50));
        isRunning = true;

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeRemaining--;
                if (timeRemaining >= 0) {
                    timeLabel.setText(timeRemaining + " seconds");
                } else if (isPreparation) {
                    isPreparation = false;
                    timeRemaining = speakTime;
                    textLabel.setText("Speaking Time:");
                    timeLabel.setText(timeRemaining + " seconds");
                    timeLabel.setForeground(new Color(220, 53, 69));
                } else {
                    timer.stop();
                    isRunning = false;
                    textLabel.setText("Time's up!");
                    timeLabel.setText("");
                }
            }
        });
        timer.start();
    }

    // Method to set preparation and speaking times based on selected part
    private void setPartTimes() {
        int selectedPart = partSelector.getSelectedIndex();
        switch (selectedPart) {
            case 0:
                prepTime = 15;
                speakTime = 45;
                break;
            case 1:
            case 2:
                prepTime = 30;
                speakTime = 60;
                break;
            case 3:
                prepTime = 20;
                speakTime = 60;
                break;
            default:
                prepTime = 15;
                speakTime = 45;
                break;
        }
    }

    // Method to reset the timer
    private void resetTimer() {
        if (timer != null) {
            timer.stop();
        }
        isRunning = false;
        textLabel.setText("Select a Part to Start");
        timeLabel.setText(" ");
        timeLabel.setForeground(new Color(50, 50, 50));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ToeflSpeakingTimer timer = new ToeflSpeakingTimer();
                timer.setVisible(true);
            }
        });
    }
}
