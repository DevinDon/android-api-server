package red.don.api.android.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import red.don.api.android.mapper.LogMapper;

@Service
public class LogService {

  @Autowired
  private LogMapper mapper;

  private long count = 0;

  public long access() {
    return count += count == 0 ? mapper.countAll() : 1;
  }

  public long getCount() {
    return count;
  }

}
