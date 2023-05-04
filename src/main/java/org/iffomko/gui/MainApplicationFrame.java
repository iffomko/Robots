package org.iffomko.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import org.iffomko.utils.localization.Localization;
import org.iffomko.utils.log.Logger;
import org.iffomko.utils.messagedFormatCached.MessageFormatting;
import org.iffomko.utils.savers.Savable;
import org.iffomko.utils.savers.SaverException;
import org.iffomko.utils.savers.StateKeeper;

/**
 * Приложение со всеми окнами
 */
public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane(); // окно с ещё окнами внутри

    /**
     * Конструктор, который создает контейнер с окнами: окно с игрой, окно с логами, генерирует меню. И настраивает их.
     */
    public MainApplicationFrame() {
        StateKeeper stateKeeper = StateKeeper.getInstance();
        Localization localization = Localization.getInstance();

        stateKeeper.restore();

        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);

        setContentPane(desktopPane);
        
        LogWindow logWindow = createLogWindow();

        try {
            logWindow.restore(stateKeeper.getState(logWindow.getPrefix()));
        } catch (SaverException e) {
            e.printStackTrace();
        }

        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();

        try {
            gameWindow.restore(stateKeeper.getState(gameWindow.getPrefix()));
        } catch (SaverException e) {
            e.printStackTrace();
        }

        addWindow(gameWindow);

        MenuBar menuBar = MenuBar.getInstance(this);

        setJMenuBar(menuBar);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onWindowClosing(e);
            }
        });

        localization.addObserver(logWindow);
        localization.addObserver(menuBar);
        localization.addObserver(gameWindow);
    }

    /**
     * Создает окно с логами и настраивает его
     * @return - возвращает объект окна с логами
     */
    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    /**
     * Добавляет фрейм в контейнер с окнами
     * @param frame - фрейм, который нужно добавить
     */
    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    /**
     * <p>
     *     Метод, который формирует всплывающее окно с выбором .jar файла с моделью и представлением робота,
     *     а затем подгружает и подменяет эти два файла
     * </p>
     */
    protected void loadRobot() {
        String packet = "org.iffomko.utils.localization.localizationProperties.mainApplicationFrame.MainApplicationFrameResource";

        MessageFormatting messageFormatting = MessageFormatting.getInstance();

        UIManager.put("FileChooser.cancelButtonText", messageFormatting.format("FileChooser.cancelButtonText", packet));
        UIManager.put("FileChooser.fileNameLabelText", messageFormatting.format("FileChooser.fileNameLabelText", packet));
        UIManager.put("FileChooser.filesOfTypeLabelText", messageFormatting.format("FileChooser.filesOfTypeLabelText", packet));
        UIManager.put("FileChooser.lookInLabelText", messageFormatting.format("FileChooser.lookInLabelText", packet));
        UIManager.put("FileChooser.folderNameLabelText", messageFormatting.format("FileChooser.folderNameLabelText", packet));

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(messageFormatting.format("selectRobotJarTitle", packet));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }

                return f.getName().endsWith(".jar");
            }

            @Override
            public String getDescription() {
                return messageFormatting.format("selectRobotJarDescription", packet);
            }
        });

        int result = fileChooser.showDialog(this, messageFormatting.format("selectRobotJarApproveBtn", packet));

        if (
                result == JFileChooser.APPROVE_OPTION &&
                        fileChooser.getSelectedFile().getPath().toLowerCase().endsWith(".jar")
        ) {
            String robotPath = fileChooser.getSelectedFile().getPath();
            System.out.println("Все супер! Мы выбрали робота!\n" + robotPath);

            // ToDo: тут надо запустить загрузчик робота
        }
    }
    
    /**
     * Метод, который дергается, когда приложение хочет закрыться
     * @param event - информация о событии
     */
    private void onWindowClosing(WindowEvent event) {
        String packet = "org.iffomko.utils.localization.localizationProperties.mainApplicationFrame.MainApplicationFrameResource";
        MessageFormatting messageFormatting = MessageFormatting.getInstance();

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        UIManager.put(
                "OptionPane.yesButtonText",
                messageFormatting.format("yesButtonText", packet)
        );
        UIManager.put(
                "OptionPane.noButtonText",
                messageFormatting.format("noButtonText", packet)
        );

        int response = JOptionPane.showOptionDialog(
                event.getWindow(),
                messageFormatting.format("closeApp", packet),
                messageFormatting.format("confirmation", packet),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null
        );

        if (response == JOptionPane.YES_OPTION) {
            StateKeeper stateKeeper = StateKeeper.getInstance();

            try {
                for (JInternalFrame frame : desktopPane.getAllFrames()) {
                    if (frame instanceof Savable) {
                        stateKeeper.addState((Savable) frame);
                    }
                }
            } catch (SaverException e) {
                e.printStackTrace();
            }

            stateKeeper.save();

            event.getWindow().setVisible(false);
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
                    new WindowEvent(event.getWindow(), WindowEvent.WINDOW_CLOSING)
            );
            setDefaultCloseOperation(EXIT_ON_CLOSE);
        }
    }

    /**
     * Устанавливает текущий стиль окошка и обновляет внешний вид от рисованного UI
     * @param className - имя класса, стиль которого нужно установить
     */
    void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }
}
