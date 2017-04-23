package servlet;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jsp.RegistrationManagementBean;
import util.Constants;
import util.DatabaseAccess;
import util.Utility;

import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Graphic;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

import datamodel.ClassManager;
import datamodel.Course;
import datamodel.CourseManager;
import datamodel.Faculty;
import datamodel.FacultyManager;
import datamodel.FamilyInfo;
import datamodel.FamilyInfoManager;
import datamodel.Relative;
import datamodel.RelativeManager;
import datamodel.Student;
import datamodel.StudentManager;
import datamodel.StudentReg;
import datamodel.StudentRegManager;
import datamodel.UniformPayment;
import datamodel.UniformPaymentManager;

public class PdfReportsServlet extends HttpServlet
{
  // CJK fonts
  private static Font hugeBoldChineseFont;
  private static Font hugeChineseFont;
  private static Font largeBoldChineseFont;
  private static Font largeChineseFont;
  private static Font normalBoldChineseFont;
  private static Font normalChineseFont;
  private static Font smallBoldChineseFont;
  private static Font smallChineseFont;

  static
  {
    try
    {
      BaseFont bfChinese = BaseFont.createFont("MSung-Light", "UniCNS-UCS2-H", BaseFont.NOT_EMBEDDED);
      hugeBoldChineseFont = new Font(bfChinese, 16, Font.BOLD);
      hugeChineseFont = new Font(bfChinese, 16, Font.NORMAL);
      largeBoldChineseFont = new Font(bfChinese, 14, Font.BOLD);
      largeChineseFont = new Font(bfChinese, 14, Font.NORMAL);
      normalBoldChineseFont = new Font(bfChinese, 12, Font.BOLD);
      normalChineseFont = new Font(bfChinese, 12, Font.NORMAL);
      smallBoldChineseFont = new Font(bfChinese, 10, Font.BOLD);
      smallChineseFont = new Font(bfChinese, 10, Font.NORMAL);
    }
    catch(Exception e)
    {
      System.out.println(e);
    }
  }

  // fonts
  private static Font hugeBoldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, Font.BOLD);
  private static Font hugeFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 16);
  private static Font largeFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14);
  private static Font normalBoldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
  private static Font normalFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12);
  private static Font smallFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10);

  private int year;
  private String schoolSemester;

  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    response.setContentType("application/pdf");
    //response.setHeader("Content-Disposition", "attachment;filename=" + type + ".pdf");

    Connection con = null;

    try
    {
      // connect to the database
      DatabaseAccess db = new DatabaseAccess();
      con = db.getConnection(Constants.DS_REF_NAME);

      String type = request.getParameter("type");
      String version = request.getParameter("version");  // for students, either simplified or detailed
      String schoolYear = request.getParameter("schoolYear");
      schoolSemester = request.getParameter("schoolSemester");
      year = Integer.parseInt(schoolYear);

      // creation of a document object for PDF
      Document doc;
      if(version != null && (version.equals("simplified") || version.equals("attendance")))
        doc = new Document(PageSize.LETTER.rotate(), 10, 10, 20, 10);  // landscape
      else
        doc = new Document(PageSize.LETTER, 10, 10, 20, 10);
      // create a PDF writer that listens to this document and writes to a
      // temporary buffer
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      PdfWriter.getInstance(doc, buffer);
      // set file properties
      doc.addAuthor("Tzu-Chi Academy");
      doc.addSubject(schoolYear + " " + schoolSemester);
      // open this document
      doc.open();

      // title image
      Image logo =
        Image.getInstance(new URL(new URL(request.getRequestURL().toString()), "webpages/images/tzuchi-academy.jpg"));

      Paragraph logoPar = null;
      Paragraph tzuchiTitlePar = null;
      Paragraph schoolYearTitlePar = null;
      Paragraph dateTitlePar = null;
      Graphic titleLine = null;
      if(type.equals("student") || type.equals("faculty"))
      {
        logoPar = new Paragraph();
        logoPar.setAlignment(Paragraph.ALIGN_CENTER);
        logo.setAlignment(Image.MIDDLE);
        logo.scalePercent(25);
        logoPar.add(new Chunk(logo, 0, -30f));
        doc.add(logoPar);

        // title
        tzuchiTitlePar = new Paragraph("\n慈濟人文學校 Tzu-Chi Academy", hugeBoldChineseFont);
        tzuchiTitlePar.setAlignment(Paragraph.ALIGN_CENTER);
        doc.add(tzuchiTitlePar);

        if(type.equals("student"))
        {
          String extra = "";
          if(version != null && version.equals("attendance"))
            extra = " 學生出席表 Student Attendance Table";

          schoolYearTitlePar =
            new Paragraph(schoolYear + " " + schoolSemester + extra, hugeBoldChineseFont);
          schoolYearTitlePar.setAlignment(Paragraph.ALIGN_CENTER);
          doc.add(schoolYearTitlePar);
        }

        dateTitlePar =
        	new Paragraph(Utility.getDateString(new java.util.Date()) + "\n\n" , smallFont);
        doc.add(dateTitlePar);

        // title line
        titleLine = new Graphic();
        titleLine.setHorizontalLine(2f, 100f);

        doc.add(titleLine);
      }

      if(type.equals("student"))
      {
        String courseIdStr = request.getParameter("courseId");
        int courseId = Integer.parseInt(courseIdStr);

        // get PDF file name
        String pdfFileName = schoolYear + schoolSemester;
        if(!Utility.isEmpty(version))
          pdfFileName = pdfFileName + "-" + version;
        if(courseId >0)
          pdfFileName = pdfFileName + "-" + courseId;
        response.setHeader("Content-Disposition", "attachment;filename=" + pdfFileName + ".pdf");

        // student related info
        if(courseId > 0)
        {
          // single course
          generateCoursePdf(con, doc, courseId, version);
        }
        else
        {
          //if(version == null !version.equals("attendance"))
          //{
            // all courses
            CourseManager courseMgr = new CourseManager();

            // chinese courses
            Vector courses =
              courseMgr.getDataBySemester(con, year, schoolSemester, ClassManager.CHINESE);

            for(int i = 0; i < courses.size(); i++)
            {
              if(i != 0)
              {
                doc.add(logoPar);
                doc.add(tzuchiTitlePar);
                doc.add(schoolYearTitlePar);
                doc.add(dateTitlePar);
                doc.add(titleLine);
              }

              Course course = (Course)courses.get(i);
              generateCoursePdf(con, doc, course.getCourseId(), version);
              doc.newPage();
            }

            // activity courses
            courses =
              courseMgr.getDataBySemester(con, year, schoolSemester, ClassManager.ACTIVITY);

            for(int i = 0; i < courses.size(); i++)
            {
              doc.add(logoPar);
              doc.add(tzuchiTitlePar);
              doc.add(schoolYearTitlePar);
              doc.add(dateTitlePar);
              doc.add(titleLine);

              Course course = (Course)courses.get(i);
              generateCoursePdf(con, doc, course.getCourseId(), version);
              doc.newPage();
            }
          //}
        }
      }
      else
        if(type.equals("faculty"))
        {
          String facultyType = request.getParameter("facultyType");

          // get PDF file name
          String pdfFileName = "faculty";
          if(!Utility.isEmpty(facultyType))
          	pdfFileName = facultyType;
          response.setHeader("Content-Disposition", "attachment;filename=" + pdfFileName + ".pdf");

        	// faculty related info
          Table facultyTable = new Table(4);
          facultyTable.setWidth(100);  // 100% of the available space
          facultyTable.setPadding(2);
          facultyTable.setDefaultHorizontalAlignment(Element.ALIGN_CENTER);
          facultyTable.setAutoFillEmptyCells(true);

          // headers
          facultyTable.addCell(new Cell(new Phrase("姓名Name", normalBoldChineseFont)), 0, 0);
          facultyTable.addCell(new Cell(new Phrase("E-Mail", normalBoldChineseFont)), 0, 1);
          facultyTable.addCell(new Cell(new Phrase("家庭電話Home Phone", normalBoldChineseFont)), 0, 2);
          facultyTable.addCell(new Cell(new Phrase("行動電話Mobile Phone", normalBoldChineseFont)), 0, 3);

          // info
          FacultyManager facultyMgr = new FacultyManager();
          Vector faculties = new Vector();
          if(!Utility.isEmpty(facultyType))
            faculties = facultyMgr.getDataByType(con, facultyType);
          else
            faculties = facultyMgr.getAllData(con);

          for(int i = 0; i < faculties.size(); i++)
          {
            Faculty faculty = (Faculty)faculties.get(i);

            String teacherName = faculty.getChineseName() + " " + faculty.getFirstName() + " " + faculty.getLastName();
            String homePhone = "";
            if(!Utility.isEmpty(faculty.getAreaCode()))
              homePhone = "(" + faculty.getAreaCode() + ")" + faculty.getPhone();

            facultyTable.addCell(new Cell(new Phrase(teacherName, normalChineseFont)));
            facultyTable.addCell(new Cell(new Phrase(faculty.getEmail(), normalFont)));
            facultyTable.addCell(new Cell(new Phrase(homePhone, normalFont)));
            facultyTable.addCell(new Cell(new Phrase(faculty.getMobilePhone(), normalFont)));
          }

          doc.add(facultyTable);
        }
        else
          if(type.equals("bill"))
          {
            String amount = request.getParameter("amount");
            String term = request.getParameter("term");
            String payment = request.getParameter("payment");
            String checkNo = request.getParameter("checkNo");
            String studentIdStr = request.getParameter("studentId");
            long studentId = Long.parseLong(studentIdStr);

            // get PDF file name
            String pdfFileName = schoolYear + schoolSemester + "-" + type;
            if(studentId >0)
              pdfFileName = pdfFileName + "-" + studentId;
            response.setHeader("Content-Disposition", "attachment;filename=" + pdfFileName + ".pdf");

            for(int i = 0; i < 2; i++)
            {
              Paragraph titlePar = new Paragraph("收據 Receipt", largeBoldChineseFont);
              titlePar.setAlignment(Paragraph.ALIGN_CENTER);
              doc.add(titlePar);

              // header
              Table headerTable = new Table(2, 1);
              headerTable.setBorder(Rectangle.NO_BORDER);
              headerTable.setWidth(100);  // 100% of the available space
              headerTable.setSpacing(0);
              String date = Utility.getDateString(new java.util.Date());
              Cell dateCell = new Cell();
              dateCell.setBorder(Rectangle.NO_BORDER);
              dateCell.add(new Paragraph("日期 Date: " + date + "\n\n\nFor " + year + " " + term, normalChineseFont));
              headerTable.addCell(dateCell, 0, 0);
              Paragraph billLogoPar = new Paragraph();
              billLogoPar.setAlignment(Paragraph.ALIGN_CENTER);
              logo.setAlignment(Image.MIDDLE);
              logo.scalePercent(20);
              billLogoPar.add(new Chunk(logo, 0, -10f));
              Cell logoCell = new Cell();
              logoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
              logoCell.setBorder(Rectangle.NO_BORDER);
              logoCell.add(billLogoPar);
              logoCell.add(new Paragraph("\n慈濟人文學校\nTzu-Chi Academy", normalChineseFont));
              headerTable.addCell(logoCell, 0, 1);
              doc.add(headerTable);

              // student name
              StudentManager stMgr = new StudentManager();
              Student st = stMgr.getDataById(con, studentId);
              String englishName = st.getFirstName() + " " + st.getLastName();
              String chineseName = st.getChineseName();
              doc.add(new Paragraph("學生姓名 Student Name:  " + chineseName + " " + englishName, normalBoldChineseFont));
              // tuition payment
              StringBuffer tuition = new StringBuffer();
              tuition.append("學費金額 Tuition Amount: $" + amount).append("  付款方式 Payment Method: " + payment);
              if(payment.equalsIgnoreCase("Check"))
                 tuition.append(" #" + checkNo);              
              doc.add(new Paragraph(tuition.toString(), normalBoldChineseFont));
              // uniform payment
              UniformPaymentManager mgr = new UniformPaymentManager();
              UniformPayment uniformPay = mgr.getPayment(con, studentId, year, schoolSemester);
              if(uniformPay != null)
              {
                 StringBuffer uniform = new StringBuffer(); 
                 uniform.append("制服金額 Uniform Amount: $" + uniformPay.getAmount());
                 String uniformPayType = Constants.PAYMENT_TYPES[uniformPay.getPaymentTypeId() - 1];
                 String uniformCheckNo = "";
                 if(uniformPayType.equalsIgnoreCase("Check"))
                    uniformCheckNo = " #" + uniformPay.getCheckNo();
                 uniform.append("  付款方式 Payment Method: " + uniformPayType + uniformCheckNo);
                 doc.add(new Paragraph(uniform.toString(), normalBoldChineseFont)); 

                 StringBuffer sizeStr = new StringBuffer();
                 String summerSize = uniformPay.getSummerSize();                 
                 String winterSize = uniformPay.getWinterSize();
                 if(!Utility.isEmpty(summerSize))
                    sizeStr.append("短袖Short Sleeves: " + summerSize + "  ");
                 if(!Utility.isEmpty(winterSize))
                    sizeStr.append("長袖Long Sleeves: " + winterSize);                 
                 if(sizeStr.length() > 0)
                    doc.add(new Paragraph(sizeStr.toString(), normalBoldChineseFont));                    
              }              
              doc.add(new Paragraph("\n\n\n收款人 Cashier:  ", normalBoldChineseFont));

              // separate line
              if(i == 0)
              {
                doc.add(new Paragraph("\n\n\n\n", normalChineseFont));
                Graphic line = new Graphic();
                line.setHorizontalLine(1f, 100f);
                doc.add(line);
                doc.add(new Paragraph("\n\n", normalChineseFont));
              }
              else
                if(i == 1)
                {
                  Paragraph copyPar = new Paragraph("\n\n學校留存\nSchool Copy", smallChineseFont);
                  copyPar.setAlignment(Paragraph.ALIGN_RIGHT);
                  doc.add(copyPar);
                }

            }
          }

      // close this document
      doc.close();

      // output the writer as bytes to the response output
      DataOutput output = new DataOutputStream(response.getOutputStream());
      byte[] bytes = buffer.toByteArray();
      response.setContentLength(bytes.length);
      for(int i = 0; i < bytes.length; i++ )
      {
        output.writeByte(bytes[i]);
      }
    }
    catch(Exception e)
    {
      System.out.println(e);
    }
    finally
    {
      try{con.close();}
      catch(Exception e){}
    }
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    doGet(request, response);
  }

  private void generateCoursePdf(Connection con, Document doc, int courseId, String version)
    throws Exception
  {
    CourseManager courseMgr = new CourseManager();
    ClassManager classMgr = new ClassManager();

    Course course = courseMgr.getDataById(con, courseId);
    int classId = course.getClassId();

    datamodel.Class cl = classMgr.getDataById(con, classId);

    // class name
    String className = cl.getName();

    // teacher and TA
    FacultyManager facultyMgr = new FacultyManager();

    long primaryTeacherId = course.getPrimaryTeacherId();
    String primaryTeacherName = "None";
    Faculty teacher = facultyMgr.getDataById(con, primaryTeacherId);
    if(teacher != null)
    {
      primaryTeacherName = "";
      if(!Utility.isEmpty(teacher.getChineseName()))
        primaryTeacherName = teacher.getChineseName() + " ";
      primaryTeacherName = primaryTeacherName + teacher.getFirstName() + " " + teacher.getLastName();
    }

    long secondaryTeacherId = course.getSecondaryTeacherId();
    String secondaryTeacherName = null;
    teacher = facultyMgr.getDataById(con, secondaryTeacherId);
    if(teacher != null)
    {
      secondaryTeacherName = "";
      if(!Utility.isEmpty(teacher.getChineseName()))
        secondaryTeacherName = teacher.getChineseName() + " ";
      secondaryTeacherName = secondaryTeacherName + teacher.getFirstName() + " " + teacher.getLastName();
    }

    String classTitle = className + "     教師Teacher: " + primaryTeacherName;
    if(!Utility.isEmpty(secondaryTeacherName))
      classTitle = classTitle + ", " + secondaryTeacherName;
    doc.add(new Paragraph("\n" + classTitle, largeBoldChineseFont));

    // simplified version
    Table simplifiedTable = new Table(6);
    if(version != null && version.equals("simplified"))
    {
      // student related info
      simplifiedTable.setWidth(100);  // 100% of the available space
      simplifiedTable.setPadding(2);
      simplifiedTable.setDefaultHorizontalAlignment(Element.ALIGN_CENTER);
      simplifiedTable.setAutoFillEmptyCells(true);

      // headers
      simplifiedTable.addCell(new Cell(new Phrase("姓名Name", smallBoldChineseFont)), 0, 0);

      if(courseMgr.getCourseCategory(con, courseId) == ClassManager.CHINESE)
        simplifiedTable.addCell(new Cell(new Phrase("課外活動Activity", smallBoldChineseFont)), 0, 1);
      else
        simplifiedTable.addCell(new Cell(new Phrase("中文課Chinese", smallBoldChineseFont)), 0, 1);

      simplifiedTable.addCell(new Cell(new Phrase("監護人Guardian", smallBoldChineseFont)), 0, 2);
      simplifiedTable.addCell(new Cell(new Phrase("監護人Guardian", smallBoldChineseFont)), 0, 3);
      simplifiedTable.addCell(new Cell(new Phrase("家庭電話Home Phone", smallBoldChineseFont)), 0, 4);
      simplifiedTable.addCell(new Cell(new Phrase("備註Note", smallBoldChineseFont)), 0, 5);
    }

    // student attendance
    int columns = 16;
    Table attendanceTable = new Table(columns);
    if(version != null && version.equals("attendance"))
    {
      // student related info
      attendanceTable.setWidth(100);  // 100% of the available space
      attendanceTable.setPadding(2);
      attendanceTable.setDefaultHorizontalAlignment(Element.ALIGN_CENTER);
      attendanceTable.setAutoFillEmptyCells(true);

      float[] colWidths = new float[columns];
      colWidths[0] = 20;
      float colWidth = (100 - colWidths[0])/(columns);
      for(int j = 1; j < columns; j++)
        colWidths[j] = colWidth;
      attendanceTable.setWidths(colWidths);
    }

    // get all students in this course
    StudentRegManager regMgr = new StudentRegManager();
    Vector students = regMgr.getStudents(con, courseId);

    FamilyInfoManager familyMgr = new FamilyInfoManager();
    RelativeManager relativeMgr = new RelativeManager();
    StudentManager stMgr = new StudentManager();
    for(int i = 0; i < students.size(); i++)
    {
      StudentReg reg = (StudentReg)students.get(i);
      long studentId = reg.getStudentId();

      if(version != null && version.equals("attendance"))
      {
        Student st = stMgr.getDataById(con, studentId);
        String studentName = st.getChineseName() + " " + st.getFirstName() + " " + st.getLastName();
        attendanceTable.addCell(new Cell(new Phrase(studentName, smallChineseFont)), i+1, 0);

        continue;
      }

      if(version != null && version.equals("simplified"))
      {
        Student st = stMgr.getDataById(con, studentId);
        String studentName = st.getChineseName() + " " + st.getFirstName() + " " + st.getLastName();

        RegistrationManagementBean regBean = new RegistrationManagementBean();

        String courseName = "";
        if(courseMgr.getCourseCategory(con, courseId) == ClassManager.CHINESE)
        {
          // activity
          int id =
            regBean.getRegisteredCourse(con, studentId, year, schoolSemester, ClassManager.ACTIVITY);
          courseName = classMgr.getNameByCourseId(con, id);
        }
        else
        {
          // Chinese
          int id =
            regBean.getRegisteredCourse(con, studentId, year, schoolSemester, ClassManager.CHINESE);
          courseName = classMgr.getNameByCourseId(con, id);
        }

        if(courseName == null)
          courseName = "";

        long familyId = st.getFamilyId();
        FamilyInfo family = familyMgr.getDataById(con, familyId);
        String homePhone = "(" + family.getAreaCode() + ")" + family.getPhone();

        String[] gdNames = new String[]{"", ""};
        for(int index = 0; index < 2; index++)
        {
          Relative rel = relativeMgr.getDataById(con, familyId, index+1);
          gdNames[index] = rel.getChineseName() + " " + rel.getFirstName() + " " + rel.getLastName();
        }

        simplifiedTable.addCell(new Cell(new Phrase(studentName, smallChineseFont)));
        simplifiedTable.addCell(new Cell(new Phrase(courseName, smallChineseFont)));
        simplifiedTable.addCell(new Cell(new Phrase(gdNames[0], smallChineseFont)));
        simplifiedTable.addCell(new Cell(new Phrase(gdNames[1], smallChineseFont)));
        simplifiedTable.addCell(new Cell(new Phrase(homePhone, smallChineseFont)));
        simplifiedTable.addCell(new Cell(new Phrase("", smallChineseFont)));
      }
      else
      {
        if(i == 0)  // extra line for the 1st one
          doc.add(new Paragraph("\n#" + (i+1), normalBoldFont));
        else
          doc.add(new Paragraph("#" + (i+1), normalBoldFont));

        // student table
        Table stTable = new Table(3, 2);
        stTable.setWidth(100);  // 100% of the available space
        stTable.setPadding(2);
        stTable.setDefaultHorizontalAlignment(Element.ALIGN_CENTER);
        stTable.setAutoFillEmptyCells(true);
        stTable.setOffset(0);

        // headers
        stTable.addCell(new Cell(new Phrase("姓名Name", normalBoldChineseFont)), 0, 0);

        if(courseMgr.getCourseCategory(con, courseId) == ClassManager.CHINESE)
          stTable.addCell(new Cell(new Phrase("課外活動Activity", normalBoldChineseFont)), 0, 1);
        else
          stTable.addCell(new Cell(new Phrase("中文課Chinese", normalBoldChineseFont)), 0, 1);

        stTable.addCell(new Cell(new Phrase("生日Date of Birth", normalBoldChineseFont)), 0, 2);

        Student st = stMgr.getDataById(con, studentId);
        String studentName = st.getChineseName() + " " + st.getFirstName() + " " + st.getLastName();
        String birthDate = Utility.getDateString(st.getBirthDate());

        RegistrationManagementBean regBean = new RegistrationManagementBean();

        String courseName = "";
        if(courseMgr.getCourseCategory(con, courseId) == ClassManager.CHINESE)
        {
          // activity
          int id =
            regBean.getRegisteredCourse(con, studentId, year, schoolSemester, ClassManager.ACTIVITY);
          courseName = classMgr.getNameByCourseId(con, id);
        }
        else
        {
          // Chinese
          int id =
            regBean.getRegisteredCourse(con, studentId, year, schoolSemester, ClassManager.CHINESE);
          courseName = classMgr.getNameByCourseId(con, id);
        }

        if(courseName == null)
          courseName = "";

        stTable.addCell(new Cell(new Phrase(studentName, normalChineseFont)), 1, 0);
        stTable.addCell(new Cell(new Phrase(courseName, normalChineseFont)), 1, 1);
        stTable.addCell(new Cell(new Phrase(birthDate, normalFont)), 1, 2);

        doc.add(stTable);

        long familyId = st.getFamilyId();
        FamilyInfo family = familyMgr.getDataById(con, familyId);
        String homePhone = "(" + family.getAreaCode() + ")" + family.getPhone();
        String email = "None";
        if(!Utility.isEmpty(family.getEmail()))
          email = family.getEmail();

        doc.add(new Paragraph("家庭電話Home Phone: " + homePhone + "     E-Mail: " + email, normalChineseFont));

        // doctor
        Relative rel = relativeMgr.getDataById(con, familyId, FamilyInfoManager.FAMILY_DOC_ID);
        String doctorName = rel.getLastName();
        String doctorPhone = rel.getWorkPhone();

        doc.add(new Paragraph("醫生Doctor: " + doctorName + "     電話Phone: " + doctorPhone, normalChineseFont));

        // guardian table
        Table gdTable = new Table(4, 3);
        gdTable.setWidth(100);  // 100% of the available space
        gdTable.setPadding(2);
        gdTable.setDefaultHorizontalAlignment(Element.ALIGN_CENTER);
        gdTable.setAutoFillEmptyCells(true);
        gdTable.setOffset(5);

        // headers
        gdTable.addCell(new Cell(new Phrase("關係Relationship", normalBoldChineseFont)), 0, 0);
        gdTable.addCell(new Cell(new Phrase("姓名Name", normalBoldChineseFont)), 0, 1);
        gdTable.addCell(new Cell(new Phrase("行動電話Mobile Phone", normalBoldChineseFont)), 0, 2);
        gdTable.addCell(new Cell(new Phrase("工作電話Work Phone", normalBoldChineseFont)), 0, 3);

        for(int id = 1; id <= 2; id++)
        {
          rel = relativeMgr.getDataById(con, familyId, id);
          String gdRelation = rel.getRelationName();
          String gdName = rel.getChineseName() + " " + rel.getFirstName() + " " + rel.getLastName();
          String gdWorkPhone = rel.getWorkPhone();
          String gdMobilePhone = rel.getMobilePhone();

          gdTable.addCell(new Cell(new Phrase(gdRelation, normalChineseFont)), id, 0);
          gdTable.addCell(new Cell(new Phrase(gdName, normalChineseFont)), id, 1);
          gdTable.addCell(new Cell(new Phrase(gdMobilePhone, normalFont)), id, 2);
          gdTable.addCell(new Cell(new Phrase(gdWorkPhone, normalFont)), id, 3);
        }

        doc.add(gdTable);
      }
    }

    if(version != null && version.equals("simplified"))
      doc.add(simplifiedTable);
    else
      if(version != null && version.equals("attendance"))
      {
        doc.add(attendanceTable);
        doc.add(new Paragraph("出席: 打勾", smallChineseFont));
        doc.add(new Paragraph("遲到: L", smallChineseFont));
        doc.add(new Paragraph("缺席: EA", smallChineseFont));
        doc.add(new Paragraph("曠課: UA", smallChineseFont));
        return;
      }

    // ending line
    doc.add(new Paragraph("\n", normalFont));
    Graphic line = new Graphic();
    line.setHorizontalLine(1f, 100f);
  }
}
