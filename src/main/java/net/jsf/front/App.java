package net.jsf.front;

import javax.swing.Box.Filler;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import net.miginfocom.swing.MigLayout;

import net.jsf.working.*;

class CellRender extends DefaultTableCellRenderer {
    public CellRender() {
        super();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
        setBorder(noFocusBorder);
        setForeground(App.primaryColor);
        return this;
    }
}


class CustomTable extends JTable {
    public CustomTable(String[][] data, String[] names) {
        super(data, names);

        DefaultTableCellRenderer cellRender = new CellRender();
        DefaultTableCellRenderer headerRender = new DefaultTableCellRenderer();

        headerRender.setBorder(null);
        headerRender.setOpaque(false);
        headerRender.setHorizontalAlignment(SwingConstants.CENTER);
        headerRender.setForeground(App.textColor);

        cellRender.setOpaque(false);
        cellRender.setHorizontalAlignment(SwingConstants.CENTER);
        cellRender.setForeground(App.textColor);

        setRowHeight(35);
        setFont(new CustomFont(0, 18));
        setDragEnabled(false);
        setOpaque(false);
        setShowHorizontalLines(true);
        setShowVerticalLines(false);
        setSize(new Dimension(870, 310));
        setDefaultEditor(Object.class, null);
        setFillsViewportHeight(true);
        setShowHorizontalLines(true);
        setShowVerticalLines(false);
        setGridColor(App.accentColor);

        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setFont(new CustomFont(1, 28));
        getTableHeader().setBackground(new Color(0, 0, 0, 0));
        getTableHeader().setForeground(App.textColor);
        getTableHeader().setDefaultRenderer(headerRender);

        super.setDefaultRenderer(Object.class, cellRender);

        setColumnWidth();
    }

    private void setColumnWidth() {
        TableColumnModel columnModel = getColumnModel();

        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setPreferredWidth(260);
        }
    }
}


class CustomFont extends Font {
    public CustomFont(int type, int size) {
        super("Poppins", type, size);
    }
}


public class App extends JFrame {
    protected static Color textColor = new Color(221, 208, 255);
    protected static Color headerColor = new Color(14, 0, 66);
    protected static Color backgroundColor = new Color(7, 0, 33);
    protected static Color primaryColor = new Color(166, 127, 254);
    protected static Color secondaryColor = new Color(155, 1, 106);
    protected static Color accentColor = new Color(253, 42, 106);
    private String titleName = "Power Biller";
    
    private Filler filler;
    private JLabel minimize; 
    private JLabel maximize;
    private JLabel exit;

    private JPanel homeLabelPanel;

    private JPanel cardPanel;
    private JPanel accountCard;
    private JPanel kwhCard;
    private JPanel amountCard;
    private JPanel tablePanel;
    private JTable table;
    
    private JTextField seekField;
    private JLabel searchLabel;

    private JPanel header;
    private JPanel sidebar;
    private JPanel seek;
    private JPanel home;
    private JPanel tableContainer;

    private JLabel numField;
    private JLabel numValue;
    private JLabel customerField;
    private JLabel customerValue;

    private JLabel currReadingField;
    private JLabel currReadingValue;
    private JLabel prevReadingField;
    private JLabel prevReadingValue;
    private JLabel kwhUsedField;
    private JLabel kwhUsedValue;

    private JLabel amountField;
    private JLabel amountValue;

    private int currentSeek;
    private int meterNum, meterReading;
    private int mouseMoveX, mouseMoveY;

    public App() {
        super.setUndecorated(true);
        super.setPreferredSize(new Dimension(1250, 800));
        super.setIconImage(new ImageIcon("src/main/resources/Icon.png").getImage());
        initComponents();
        pack();
    }

    private void createHeader() {
        header = new JPanel(new MigLayout("wrap, fill, align 25% 50%", "190px[][]"));
        JPanel container = new JPanel(new MigLayout("wrap, align 50% 50%, flowy", "[]30px[]30px[]30px"));
        filler = new Filler(getMinimumSize(), getPreferredSize(), getMaximumSize());
        minimize = createHeaderButton("//");
        maximize = createHeaderButton("O");
        exit = createHeaderButton("X");
        JLabel title = new JLabel(titleName);

        header.setPreferredSize(new Dimension(getWidth(), 35));
        header.setBackground(headerColor);

        container.setBackground(headerColor);

        filler.setBackground(headerColor);
        
        title.setFont(new CustomFont(1, 15));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setForeground(textColor);

        container.add(minimize);
        container.add(maximize);
        container.add(exit);

        header.add(title, "dock center");
        header.add(container, "dock east");

        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                mouseMoveX = evt.getX();
                mouseMoveY = evt.getY();
            }
        });

        header.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent evt) {
                setLocation(evt.getXOnScreen() - mouseMoveX, evt.getYOnScreen() - mouseMoveY);
            }
        });

        add(header, BorderLayout.NORTH);
    }

    private JLabel createHeaderButton(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new CustomFont(1, 15));
        label.setForeground(textColor);

        label.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                evt.getComponent().setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent evt) {
                evt.getComponent().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            public void mouseClicked(MouseEvent evt) {
                if (label.getText().equals("//")) {
                    setExtendedState(JFrame.ICONIFIED);
                }
                else if (label.getText().equals("O")) {
                    if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                        setExtendedState(JFrame.NORMAL);
                    }
                    else {
                        setExtendedState(JFrame.MAXIMIZED_BOTH);
                    }
                }
                else if (label.getText().equals("X")) {
                    dispose();
                    System.exit(0);
                }
            } 
        });

        return label;
    }

    private void createSidebar() {
        sidebar = new RoundPanel(headerColor, accentColor, 0);
        sidebar.setLayout(new MigLayout("wrap, align 50% 10%, gapy 26, flowx"));
        sidebar.setPreferredSize(new Dimension(200, getHeight() - 35));
        sidebar.setOpaque(false);

        homeLabelPanel = createSidebarPanel("Home", "src/main/resources/Home.png");

        sidebar.add(homeLabelPanel);

        add(sidebar, BorderLayout.WEST);
    }

    private JPanel createSidebarPanel(String text, String filename) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(text, smoothen(filename), JLabel.CENTER);

        panel.setBackground(new Color(0, 0, 0, 0));
        label.setFont(new CustomFont(1, 24));
        label.setForeground(textColor);

        panel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                evt.getComponent().setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent evt) {
                evt.getComponent().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            public void mouseClicked(MouseEvent evt) {
                if (evt.getComponent().equals(homeLabelPanel)) {
                    createHome();
                }
            }
        });

        panel.add(label);

        return panel;
    }

    private void createSeek() {
        seek = new RoundPanel(secondaryColor, accentColor, 15);
        seekField = new JTextField("Enter your Meter Number");
        searchLabel = new JLabel(smoothen("src/main/resources/Search.png"));
        seek.setPreferredSize(new Dimension(955, 50));
        seek.setLayout(new MigLayout("wrap, align 5%", "25px[][]25px"));
        seek.setOpaque(false);
        
        seekField.setFont(new CustomFont(0, 20));
        seekField.setPreferredSize(new Dimension(seek.getWidth() - 5, 32));
        seekField.setForeground(textColor);
        seekField.setOpaque(false);
        seekField.setBorder(null);
        seekField.setColumns(50);
        seekField.setHorizontalAlignment(JTextField.CENTER);

        seekField.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                evt.getComponent().setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseClicked(MouseEvent evt) {
                ((JTextField) evt.getComponent()).selectAll();
            }
            public void mouseExited(MouseEvent evt) {
                evt.getComponent().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        seekField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                if (Character.isDigit(evt.getKeyChar())) {
                    if (((JTextField) evt.getComponent()).getText().equals("Enter your Meter Number") || ((JTextField) evt.getComponent()).getText().equals("Enter your Meter Reading") || ((JTextField) evt.getComponent()).getText().equals("")) {
                        ((JTextField) evt.getComponent()).setText("");
                    }
                }
            }
        });

        searchLabel.setFont(new CustomFont(1, 35));
        searchLabel.setForeground(textColor);

        searchLabel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                evt.getComponent().setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent evt) {
                evt.getComponent().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            public void mouseClicked(MouseEvent evt) {
                if (currentSeek == 0) {
                    if (seekField.getText().length() != 6) {
                        showSeekError("Meter Number is invalid", "Enter your Meter Number");
                    }

                    else if (seekField.getText().charAt(0) == '0') {
                        showSeekError("Meter Number is invalid", "Enter your Meter Number");
                    }
                    
                    else if (seekField.getText().equals("000000")) {
                        showSeekError("Meter Number is invalid", "Enter your Meter Number");
                    }
                    try {
                        meterNum = Integer.parseInt(seekField.getText());

                        seekField.setText("Enter your Meter Reading");
                        currentSeek = 1;
                    }
                    catch (NumberFormatException numExc) {
                        showSeekError("Meter Number is invalid", "Enter your Meter Number");
                    }
                }

                else if (currentSeek == 1) {
                    if (seekField.getText().length() != 5) {
                        showSeekError("Meter Reading is invalid", "Enter your Meter Reading");
                    }

                    else if (seekField.getText().charAt(0) == '0') {
                        showSeekError("Meter Reading is invalid", "Enter your Meter Reading");
                    }

                    else if (seekField.getText().equals("00000")) {
                        showSeekError("Meter Reading is invalid", "Enter your Meter Reading");
                    }

                    try {
                        meterReading = Integer.parseInt(seekField.getText());
    
                        seekField.setText("Enter your Meter Number");

                        Biller biller = new Biller(meterNum, meterReading);

                        String[] account = biller.get();

                        if (meterReading != Integer.parseInt(account[1])) {
                            String[] newAcc = account.clone();

                            newAcc[1] = String.valueOf(meterReading);

                            new Reader().update(account, newAcc);
                        }

                        int usedKwh = biller.calculateKilowatt(account);
                        double payAmount = biller.calculatePayAmount(usedKwh, account);

                        numValue.setText(String.valueOf(meterNum));
                        customerValue.setText(account[2].toUpperCase());

                        currReadingValue.setText(String.format("%,d", meterReading));
                        prevReadingValue.setText(String.format("%,d", Integer.parseInt(account[1])));
                        kwhUsedValue.setText(String.format("%,d KW/H", usedKwh));

                        amountValue.setText(String.format("Php. %1$,.2f", payAmount));
                            
                        numValue.repaint();
                        customerValue.repaint();
                        currReadingValue.repaint();
                        prevReadingValue.repaint();
                        kwhUsedValue.repaint();
                        amountValue.repaint();

                        home.remove(tableContainer);
                        createTablePanel();
                        home.repaint();
                        currentSeek = 0;
                    }
                    catch (NumberFormatException numExc) {
                        showSeekError("Meter Reading is invalid", "Enter your Meter Reading");
                    }
                }
            }
        });

        seek.add(seekField);
        seek.add(searchLabel);


        home.add(seek);
    }

    private void showSeekError(String message1, String message2) {
        seekField.setText(message1);
        seekField.setForeground(headerColor);

        Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                seekField.setText(message2);
                seekField.setForeground(textColor);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    private void createHome() {
        home = new JPanel(new MigLayout("wrap, align 50% 50%", "", "10px[][][]10px"));
        home.setPreferredSize(new Dimension(getWidth() - 200, getHeight() - 35));
        home.setBackground(backgroundColor);

        createSeek();
        createCardPanel();
        createTablePanel();

        add(home, BorderLayout.CENTER);
    }

    private void createCardPanel() {
        cardPanel = new JPanel(new MigLayout("wrap, align 50% 50%", "[]25px[]25px[]", "50px[]50px"));
        cardPanel.setBackground(new Color(0, 0, 0, 0));

        accountCard = createCard(
            new MigLayout("wrap, align 50%", "", "5px[]10px[]5px[]10px[]5px[]5px"),
            "src/main/resources/Account.png");
        
        numField = createField("Meter Number");
        numValue = createValue("--------");

        customerField = createField("Customer Type");
        customerValue = createValue("--------");

        accountCard.add(numField);
        accountCard.add(numValue);

        accountCard.add(customerField);
        accountCard.add(customerValue);
        
        kwhCard = createCard(
            new MigLayout("wrap, align 50%", "", "5px[]10px[]5px[]10px[]5px[]10px[]5px[]0px"),
            "src/main/resources/KWH.png");
        
        currReadingField = createField("Curr. Meter Reading");
        currReadingValue = createValue("--------");

        prevReadingField = createField("Prev. Meter Reading");
        prevReadingValue = createValue("--------");   

        kwhUsedField = createField("Kilowatts Used");
        kwhUsedValue = createValue("--------");

        kwhCard.add(currReadingField);
        kwhCard.add(currReadingValue);
        kwhCard.add(prevReadingField);
        kwhCard.add(prevReadingValue);
        kwhCard.add(kwhUsedField);
        kwhCard.add(kwhUsedValue);

        amountCard = createCard(
            new MigLayout("wrap, align 50%", "", "5px[]35px[]5px[]"), 
            "src/main/resources/Amount.png");

        amountField = createField("Amount to Pay");
        amountValue = createValue("--------");

        amountCard.add(amountField);
        amountCard.add(amountValue);

        cardPanel.add(accountCard);
        cardPanel.add(kwhCard);
        cardPanel.add(amountCard);

        home.add(cardPanel);
    }

    private JPanel createCard(LayoutManager layout, String filename) {
        JPanel panel = new RoundPanel(secondaryColor, accentColor, 20);
        panel.setLayout(layout);
        panel.setPreferredSize(new Dimension(300, 275));
        panel.setOpaque(false);
        JLabel logo = new JLabel(smoothen(filename));
        logo.setFont(new CustomFont(1, 60));
        
        panel.add(logo, "gapleft 220");

        return panel;
    }

    private JLabel createField(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new CustomFont(1, 22));
        label.setForeground(textColor);

        return label;
    }

    private JLabel createValue(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new CustomFont(0, 18));
        label.setForeground(textColor);

        return label;   
    }
    
    private void createTablePanel() {
        tablePanel = new RoundPanel(backgroundColor, headerColor, 15);
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.setPreferredSize(new Dimension(955, 230));

        ArrayList<String[]> readData = new Reader().read();
        String[][] array2dData = new String[readData.size()][readData.get(0).length];
        String[] tableHeaders = {"Meter Number", "Current Meter Reading", "Customer Type"};

        for (String[] d: readData) {
            array2dData[readData.indexOf(d)] = new String[]{d[0], String.format("%,d", Integer.valueOf(d[1])), d[2].toUpperCase()};
        }

        table = new CustomTable(array2dData, tableHeaders);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new EmptyBorder(0, 0, 0, 0));
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        tablePanel.add(scroll, BorderLayout.CENTER);
        
        tableContainer = new JPanel();
        tableContainer.setBackground(new Color(0, 0, 0, 0));
        tableContainer.setOpaque(false);
        tableContainer.add(tablePanel);

        home.add(tableContainer);
    }

    private ImageIcon smoothen(String filename) {
        BufferedImage imageResize = new BufferedImage(45, 45, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = imageResize.createGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.drawImage(new ImageIcon(filename).getImage(), 0, 0, 45, 45, null);
        graphics.dispose();

        return new ImageIcon(imageResize);
    }

    private void initComponents() {
        createHeader();
        createSidebar();
        createHome();
    }
}
