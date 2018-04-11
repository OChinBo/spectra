package cn.edu.pku.dao;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.*;

import cn.edu.pku.entity.ProjectEntity;
import cn.edu.pku.util.CryptUtils;
import cn.edu.pku.util.OpenFileUtils;
import cn.edu.pku.util.PropertiesUtils;


/** WARNING: THIS CLASS IS NOT FINISHED YET */
public class ProjectDao extends Dao<ProjectEntity, ProjectEntity> {

	File data = null;

	// Constructor
	ProjectDao(File data) {
		this.data = data;
	}



	@Override
	public ProjectEntity read() {

		JSONObject jsonObj = null;
		ProjectEntity project = new ProjectEntity();

		try {
			BufferedReader in = new BufferedReader(new FileReader(data));
			String inputStr = in.toString();

			// Check config from properties
			if(PropertiesUtils.readConfig().get("encrypt").equals("true")){
				inputStr = CryptUtils.Decrypt(inputStr);
			}
			jsonObj = new JSONObject(CryptUtils.Decrypt(inputStr));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}


		return project;
	}

	@Override
	public void write(ProjectEntity data) {

		System.out.println(data.toJson());


	}

}
