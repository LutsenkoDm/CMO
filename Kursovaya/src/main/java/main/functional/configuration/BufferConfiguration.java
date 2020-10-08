package main.functional.configuration;

import org.springframework.stereotype.Component;

@Component
public class BufferConfiguration
{
  private int maxSize;

  public BufferConfiguration()
  {
    maxSize = 5;
  }

  public BufferConfiguration(int maxSize)
  {
    this.maxSize = maxSize;
  }

  public int getMaxSize()
  {
    return maxSize;
  }

  public void setMaxSize(int maxSize)
  {
    this.maxSize = maxSize;
  }

  @Override
  public String toString()
  {
    return "BufferConfiguration{" + "maxSize=" + maxSize + '}';
  }
}
