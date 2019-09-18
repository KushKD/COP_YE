package com.kushkumardhawan.inspections.Enums;

public enum TaskType {

    GET_USER(1),
    UPLOAD_FILES(2),
    GET_DEPARTMENTS(3),
    POST_FORM_DATA(4);
    int value; private TaskType(int value) { this.value = value; }


}