package com.Course.Interface;

import java.util.List;
import java.util.Map;
import com.Course.db.Coursedb;

public interface Lesson_DetailInterface {
   public void getData(List<Map<String, Object>> Data);
   public void getCourseData(List<Coursedb> coursedb);
}
