package com.AWSManagementAntTask;

import com.AWSManagementAntTask.ant.task.ExportAWSInfoTask;

public class Main {

	public static void main(String[] args) {
		ExportAWSInfoTask action = new ExportAWSInfoTask();
		action.execute();
	}
}
