package backend.academy.interfaces;

import backend.academy.entities.LogReport;

public interface FileWriter {
    void writeFile(String fileName, LogReport report);
}
