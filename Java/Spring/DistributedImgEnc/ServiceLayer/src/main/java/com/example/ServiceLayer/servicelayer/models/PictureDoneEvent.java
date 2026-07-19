package com.example.ServiceLayer.servicelayer.models;

public class PictureDoneEvent {

  private String id;
  private String name;
  private Status status;
  private String error;

  public PictureDoneEvent() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }
}
