package com.example.ServiceLayer.servicelayer.models;

import java.io.File;
import java.time.Instant;

public class PictureEvent {
  private String id;
  private Status status;
  private String name;
  private Instant createdAt = Instant.now();
  private OPPERATION opperation;
  private byte[] asciiKey;

  public OPPERATION getOpperation() {
    return opperation;
  }

  public void setOpperation(OPPERATION opperation) {
    this.opperation = opperation;
  }

  public byte[] getAsciiKey() {
    return asciiKey;
  }

  public void setAsciiKey(byte[] asciiKey) {
    this.asciiKey = asciiKey;
  }

  public PictureEvent() {
  }

  public enum OPPERATION {
    ENCRYPTION("enc"),
    DECRYPTION("dec");

    private final String label;

    public String getLabel() {
      return this.label;
    }

    private OPPERATION(String label) {
      this.label = label;
    }
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

}
