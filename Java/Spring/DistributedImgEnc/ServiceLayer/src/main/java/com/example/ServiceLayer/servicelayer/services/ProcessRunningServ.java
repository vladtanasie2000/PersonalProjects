package com.example.ServiceLayer.servicelayer.services;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ProcessRunningServ {
  private final String imgEng;

  public ProcessRunningServ(@Value("${img-enc}") String imgEnc) {
    this.imgEng = imgEnc;
  }

  public int run(String[] commands) {
    List<String> command = new ArrayList<>();
    command.add(imgEng);
    command.addAll(Arrays.asList(commands));
    ProcessBuilder pb = new ProcessBuilder();
    pb.command(command);
    Process process;
    int exitCode = -1;
    try {
      process = pb.start();
      exitCode = process.waitFor();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return exitCode;
  }
}
