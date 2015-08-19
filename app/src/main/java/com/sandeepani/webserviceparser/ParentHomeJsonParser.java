package com.sandeepani.webserviceparser;

import android.util.Log;

import com.sandeepani.model.AddressModel;
import com.sandeepani.model.ParentModel;
import com.sandeepani.model.StudentDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Vijay on 3/21/15.
 */
public class ParentHomeJsonParser {
    public static ParentHomeJsonParser parentHomeJsonParser = null;

    private ParentHomeJsonParser() {

    }

    public static ParentHomeJsonParser getInstance() {
        if (parentHomeJsonParser == null) {
            parentHomeJsonParser = new ParentHomeJsonParser();
        }
        return parentHomeJsonParser;
    }

    public ParentModel getParentDetails(JSONArray jsonArray) {
        ParentModel parentModel = new ParentModel();
        try {
            JSONObject parentObj = jsonArray.getJSONObject(0);
            if (parentObj.has("educational_qualification")) {
                parentModel.setEducationalQualification(parentObj.getString("educational_qualification"));
            }
            if (parentObj.has("emailId")) {
                parentModel.setEmail(parentObj.getString("emailId"));
            }
            if (parentObj.has("username")) {
                parentModel.setUsername(parentObj.getString("username"));
            }
            if (parentObj.has("profession")) {
                parentModel.setProfession(parentObj.getString("profession"));
            }
            if (parentObj.has("name")) {
                parentModel.setName(parentObj.getString("name"));
            }
            if (parentObj.has("officeNumber")) {
                parentModel.setOfficeNumber(parentObj.getString("officeNumber"));
            }
            if (parentObj.has("mobileNumber")) {
                parentModel.setMobileNumber(parentObj.getString("mobileNumber"));
            }
            if (parentObj.has("children")) {
                JSONArray childrenArray = parentObj.getJSONArray("children");
                int childCount = childrenArray.length();
                parentModel.setNumberOfChildren(childCount);
                ArrayList<StudentDTO> childsList = new ArrayList<StudentDTO>();
                for (int i = 0; i < childCount; i++) {
                    JSONObject childObj = childrenArray.getJSONObject(i);
                    StudentDTO studentDTO = new StudentDTO();
                    if (childObj.has("studentId")) {
                        studentDTO.setStudentId(Integer.parseInt(childObj.getString("studentId")));
                    }
                    if (childObj.has("dob")) {
                        studentDTO.setDob(childObj.getString("dob"));
                    }
                    if (childObj.has("studentName")) {
                        studentDTO.setStundentName(childObj.getString("studentName"));
                    }
                    if (childObj.has("grade")) {
                        studentDTO.setGrade(childObj.getString("grade"));
                    }
                    if (childObj.has("present_guardian")) {
                        studentDTO.setPresentGuadian(childObj.getString("present_guardian"));
                    }
                    if (childObj.has("gender")) {
                        studentDTO.setGender(childObj.getString("gender"));
                    }
                    if (childObj.has("studentPhoto")) {
                        studentDTO.setStudentPhoto(childObj.getString("studentPhoto"));
                    }
                    if (childObj.has("registerNumber")) {
                        studentDTO.setRegisterNumber(childObj.getString("registerNumber"));
                    }
                    if (childObj.has("local_guardian")) {
                        //   studentDTO.setLocalGuardian(childObj.getString("local_guardian"));
                    }
                    if (childObj.has("section")) {
                        studentDTO.setSection(childObj.getString("section"));
                    }
                    if (childObj.has("present_address")) {
                        JSONObject addressObj = childObj.getJSONObject("present_address");
                        AddressModel addressModel = new AddressModel();
                        if (addressObj.has("place")) {
                            addressModel.setPlace(addressObj.getString("place"));
                        }
                        if (addressObj.has("landmark")) {
                            addressModel.setLandmark(addressObj.getString("landmark"));
                        }
                        if (addressObj.has("address")) {
                            addressModel.setAddress(addressObj.getString("address"));
                        }
                        studentDTO.setAddressModel(addressModel);
                        addressModel = null;
                    }
                    childsList.add(studentDTO);
                    studentDTO = null;
                }
                parentModel.setChildList(childsList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parentModel;
    }

//    public static ArrayList<HashMap<String, String>> getChildrenListwithID(JSONArray jsonArray) {
//        ArrayList<HashMap<String, String>> childrenNameAndIdArraylist = null;
//        LinkedHashMap<String, String> childrenNameAndIdHashMap = null;
//        CustomDialogueAdapter customDialogueAdapter ;
//        if (jsonArray != null) {
//            int size = jsonArray.length();
//            for (int i = 0; i < size; i++) {
//                try {
//                    JSONObject object = jsonArray.getJSONObject(i);
//                    JSONArray childrenArray = object.getJSONArray("children");
//                    childrenNameAndIdArraylist = new ArrayList<HashMap<String, String>>();
//                    for(int j= 0; j<childrenArray.length();j++ ){
//                        childrenNameAndIdHashMap = new LinkedHashMap<String, String>();
//                        JSONObject noOfStudent = childrenArray.getJSONObject(j);
//                        if (noOfStudent.has("studentId")) {
//                            childrenNameAndIdHashMap.put("studentId", noOfStudent.getString("studentId"));
//                        }
//                        if (noOfStudent.has("studentName")) {
//                            childrenNameAndIdHashMap.put("studentName", noOfStudent.getString("studentName"));
//                        }
//                        childrenNameAndIdArraylist.add(childrenNameAndIdHashMap);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        //customDialogueAdapter = new CustomDialogueAdapter(activity,childrenNameAndIdArraylist);
//        Log.i("Childata", childrenNameAndIdArraylist.toString());
//        return childrenNameAndIdArraylist;
//    }


    public static ArrayList<HashMap<String, String>> getChildrenGradeAndSection(JSONArray jsonArray) {
        ArrayList<HashMap<String, String>> childrenGradeAndSection = null;
        LinkedHashMap<String, String> childrenGradeAndSectionMap = null;
        try {
            if (jsonArray != null) {
                int size = jsonArray.length();
                for (int i = 0; i < size; i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    if (object.has("children")) {
                        JSONArray childrenArray = object.getJSONArray("children");
                        childrenGradeAndSection = new ArrayList<HashMap<String, String>>();
                        for (int j = 0; j < childrenArray.length(); j++) {
                            childrenGradeAndSectionMap = new LinkedHashMap<String, String>();

                            JSONObject childName = childrenArray.getJSONObject(j);
                            if (childName.has("studentName")) {
                                String studentName = childName.getString("studentName");
                                childrenGradeAndSectionMap.put("studentName", studentName);
                            }
                            if (childName.has("grade")) {
                                String grade = childName.getString("grade");
                                childrenGradeAndSectionMap.put("grade", grade);
                            }
                            if (childName.has("section")) {
                                String section = childName.getString("section");
                                childrenGradeAndSectionMap.put("section", section);
                            }
                            // Log.i("childrenGradeSectionMap", childrenGradeAndSectionMap.toString());
                            childrenGradeAndSection.add(childrenGradeAndSectionMap);
                            // Log.i("childrenGradeAndSection", childrenGradeAndSection.toString());
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("childrenGradeAndSection", childrenGradeAndSection.toString());
        return childrenGradeAndSection;
    }

}
