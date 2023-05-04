package org.iffomko.utils.localization.localizationProperties.mainApplicationFrame;

import java.util.ListResourceBundle;

/**
 * <p>Класс, который отвечает за локализацию компоненты <code>MainApplicationFrame</code>> для русского языка/p>
 */
public class MainApplicationFrameResource_ru_RU extends ListResourceBundle {
    /**
     * <p>
     *     Возвращает массив пар ключ, значение, где ключ обязан быть <code>String</code>, а значение это любой тип, который
     *     наследуется от <code>Object</code>. Ключ хранится на 0 индексе, а значение на 1.
     * </p>
     *
     * @return массив типа <code>Object</code>, который представляет собой массив пар ключ-значение
     */
    @Override
    protected Object[][] getContents() {
        return new Object[][] {
                {"yesButtonText", "Да"},
                {"noButtonText", "Нет"},
                {"closeApp", "Закрыть приложение?"},
                {"confirmation", "Подтверждение"},
                {"FileChooser.cancelButtonText", "Отменить"},
                {"FileChooser.fileNameLabelText", "Имя файла"},
                {"FileChooser.filesOfTypeLabelText", "Тип файла"},
                {"FileChooser.lookInLabelText", "Директория"},
                {"FileChooser.folderNameLabelText", "Название папки"},
                {"selectRobotJarDescription", "Только .jar файлы"},
                {"selectRobotJarApproveBtn", "Выбрать"},
                {"selectRobotJarTitle", "Загрузить .jar файл"}
        };
    }
}