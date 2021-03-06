package com.liu233w.encryption.encryptedChating.client.ui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.SimpleTerminalResizeListener;
import com.googlecode.lanterna.terminal.Terminal;
import com.liu233w.encryption.encryptedChating.securityConnection.SecurityConnection;
import com.liu233w.encryption.encryptedChating.securityConnection.WrongSignatureException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

public class ChatWindow {

    private SecurityConnection connection;
    private TextBox chattingInput;
    private TextBox messageBox;
    private SimpleDateFormat simpleDateFormat;
    private String destAddress;

    public ChatWindow(SecurityConnection connection) {
        this.connection = connection;
        destAddress = connection.getDestAddress();
        simpleDateFormat = new SimpleDateFormat(" [yyyy-MM-dd HH:mm:ss] ");
    }

    public void start() throws IOException {

        final DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        final TerminalScreen screen = defaultTerminalFactory.createScreen();
        screen.startScreen();

        final TerminalSize terminalSize = screen.getTerminalSize();
        screen.getTerminal().addResizeListener(new SimpleTerminalResizeListener(terminalSize) {
            @Override
            public synchronized void onResized(Terminal terminal, TerminalSize newSize) {
                handleResize(newSize);
            }
        });

        final MultiWindowTextGUI textGUI = new MultiWindowTextGUI(screen);
        final BasicWindow window = buildGui(terminalSize);

        new Thread(() -> {
            while (true) {
                receiveMessage();
            }
        }).start();

        textGUI.addWindowAndWait(window);
    }

    private BasicWindow buildGui(TerminalSize size) {

        final BasicWindow basicWindow = new BasicWindow();

        final Panel mainPanel = new Panel();
        mainPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL).setSpacing(1));

        messageBox = new TextBox("Welcome! Press TAB to move focus between message box and input field")
                .setReadOnly(true)
                .setVerticalFocusSwitching(false)
                .addTo(mainPanel);

        chattingInput = new TextBox() {
            @Override
            public synchronized Result handleKeyStroke(KeyStroke keyStroke) {

                if (keyStroke.getKeyType().equals(KeyType.Enter)) {
                    sendMessage();
                    return Result.HANDLED;
                }

                return super.handleKeyStroke(keyStroke);
            }
        };
        chattingInput.takeFocus();
        new Panel()
                .addComponent(new Label("Your message(press enter to send):"))
                .addComponent(chattingInput)
                .addTo(mainPanel);

        basicWindow.setComponent(mainPanel);
        final HashSet<Window.Hint> hints = new HashSet<>();
        hints.add(Window.Hint.FULL_SCREEN);
        basicWindow.setHints(hints);

        handleResize(size);

        return basicWindow;
    }

    private void handleResize(TerminalSize size) {
        messageBox.setPreferredSize(new TerminalSize(size.getColumns() - 2, size.getRows() - 5));
        chattingInput.setPreferredSize(new TerminalSize(size.getColumns() - 2, 1));
    }

    private void sendMessage() {
        final String text = chattingInput.getText();
        if (!text.isEmpty()) {
            chattingInput.setText("");
            addMessageToList("You", text);

            new Thread(() -> {
                try {
                    connection.send(stringToUtf8(text));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private synchronized void addMessageToList(String from, String content) {
        final StringBuilder messageText = new StringBuilder();
        messageText.append(from);
        messageText.append(simpleDateFormat.format(new Date()));
        messageText.append("> ");
        messageText.append(content);

        messageBox.addLine(messageText.toString());
        messageBox.handleKeyStroke(new KeyStroke(KeyType.ArrowDown));
        messageBox.handleKeyStroke(new KeyStroke(KeyType.ArrowDown));
    }

    private void receiveMessage() {
        try {
            final byte[] recv = connection.recv();
            addMessageToList(destAddress, utf8ToString(recv));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WrongSignatureException e) {
            addMessageToList(destAddress + "(Unauthorized)", utf8ToString(e.getData()));
        }
    }

    private static String utf8ToString(byte[] data) {
        return new String(data, Charset.forName("utf-8"));
    }

    private static byte[] stringToUtf8(String str) {
        return str.getBytes(Charset.forName("utf-8"));
    }
}
