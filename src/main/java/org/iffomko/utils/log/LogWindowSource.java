package org.iffomko.utils.log;

import org.iffomko.utils.notes.Notes;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Что починить:
 * 1. Этот класс порождает утечку ресурсов (связанные слушатели оказываются
 * удерживаемыми в памяти)
 * 2. Этот класс хранит активные сообщения лога, но в такой реализации он 
 * их лишь накапливает. Надо же, чтобы количество сообщений в логе было ограничено 
 * величиной m_iQueueLength (т.е. реально нужна очередь сообщений 
 * ограниченного размера) 
 */
public class LogWindowSource
{
    private int m_iQueueLength;
    
    private Notes<LogEntry> m_messages;
    private final ArrayList<LogChangeListener> m_listeners;
    private volatile LogChangeListener[] m_activeListeners;
    
    public LogWindowSource(int iQueueLength) 
    {
        m_iQueueLength = iQueueLength;
        m_messages = new Notes<>(iQueueLength);
        m_listeners = new ArrayList<LogChangeListener>();
    }

    /**
     * Добавляет слушателя в массив со слушателями
     * @param listener - слушатель
     */
    public void registerListener(LogChangeListener listener)
    {
        synchronized(m_listeners)
        {
            m_listeners.add(listener);
            m_activeListeners = null;
        }
    }

    /**
     * Удаляет слушателя из массива
     * @param listener - слушатель
     */
    public void unregisterListener(LogChangeListener listener)
    {
        synchronized(m_listeners)
        {
            m_listeners.remove(listener);
            m_activeListeners = null;
        }
    }

    /**
     * Добавляет новый лог в список, уведомляет всех слушателей лога
     * @param logLevel - уровень добавляемого лога
     * @param strMessage - сообщение лога
     */
    public void append(LogLevel logLevel, String strMessage)
    {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        m_messages.add(entry);
        LogChangeListener[] activeListeners = m_activeListeners;
        if (activeListeners == null)
        {
            synchronized (m_listeners)
            {
                if (m_activeListeners == null)
                {
                    activeListeners = m_listeners.toArray(new LogChangeListener [0]);
                    m_activeListeners = activeListeners;
                }
            }
        }
        for (LogChangeListener listener : activeListeners)
        {
            listener.onLogChanged();
        }
    }

    /**
     * Размер всех сообщения лога
     * @return - размер
     */
    public int size()
    {
        return m_messages.size();
    }

    /**
     * Возвращает перебираемый объект с логами начиная с задаваемого индекса
     * @param startFrom - индекс, с которого надо вернуть подсписок
     * @param count - количество элементов в подсписке
     * @return - перебираемый объект
     */
    public Iterable<LogEntry> range(int startFrom, int count)
    {
        if (startFrom < 0 || startFrom >= m_messages.size())
        {
            return Collections.emptyList();
        }
        int indexTo = Math.min(startFrom + count, m_messages.size());
        return m_messages.getSegment(startFrom, indexTo);
    }

    /**
     * Возвращает все логи
     * @return - перебираемый объект
     */
    public Iterable<LogEntry> all()
    {
        return m_messages;
    }
}
