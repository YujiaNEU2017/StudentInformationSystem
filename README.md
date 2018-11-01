# StudentInformationSystem

* Programs:  
Get all: /programs  
Get one: /programs/{programName}  
Post: /programs   
{  
&nbsp;&nbsp;"programName": "{programName}",  
&nbsp;&nbsp;"courses": [],  
&nbsp;&nbsp;"students": []  
}  
Put: /programs/{programName}  
{  
&nbsp;&nbsp;"courses": [],  
&nbsp;&nbsp;"students": []  
}  
Delete: /programs/{programName}  

* Courses:  
Get all: /courses  
Get by programName: /courses?programName=XXX  
Get one: /courses/{courseId}  
Post: /courses   
{  
&nbsp;&nbsp;"courseId": "{courseId}",  
&nbsp;&nbsp;"lectures": [],  
&nbsp;&nbsp;"board": "",  
&nbsp;&nbsp;"roster": [],  
&nbsp;&nbsp;"professorId": 0, (set 0 for an empty value)  
&nbsp;&nbsp;"studentTAId": 0, (set 0 for an empty value)  
&nbsp;&nbsp;"programName": "{programName}"  (set null for an empty vale)   
}   
Put: /courses/{courseId}   
{  
&nbsp;&nbsp;"lectures": [],  
&nbsp;&nbsp;"board": "",  
&nbsp;&nbsp;"roster": [],  
&nbsp;&nbsp;"professorId": 0, (set 0 for an empty value)  
&nbsp;&nbsp;"studentTAId": 0, (set 0 for an empty value)  
&nbsp;&nbsp;"programName": "{programName}"  (set null for an empty vale)  
}   
Delete: /courses/{courseId}  
Get board: /courses/{courseId}/board   
Put board: /courses/{courseId}/board  
{  
&nbsp;&nbsp;"board": "XXXX"  
}  
Get roster: /courses/{courseId}/roster  
Put roster: /courses/{courseId}/roster  
{  
&nbsp;&nbsp;"roster": []  
}  

* Lectures:  
Get all: /lectures  
Get by courseId: /lectures?courseId=XXX  
Get one: /lectures/{lectureId}  
Post: /lectures   
{  
&nbsp;&nbsp;"courseId": "{courseId}",  (set null for an empty vale)  
&nbsp;&nbsp;"material": "XXXX",  
&nbsp;&nbsp;"notes": "XXXX"  
}   
Put: /lectures/{lectureId}    
{  
&nbsp;&nbsp;"courseId": "{courseId}",  (set null for an empty vale)  
&nbsp;&nbsp;"material": "XXXX",  
&nbsp;&nbsp;"notes": "XXXX"  
}   
Delete: /lectures/{lectureId}  

* Students:  
Get all: /students  
Get by programName and/or courseId: /students?programName=XXX&courseId=XXX   
Get one: /students/{studentId}  
Post: /students  
{  
&nbsp;&nbsp;"name": "XXX",  
&nbsp;&nbsp;"imageURL":"XXX",  
&nbsp;&nbsp;"coursesAssisted": [],  
&nbsp;&nbsp;"coursesEnrolled": [],  
&nbsp;&nbsp;"programName": "{programName}"  (set null for an empty vale)  
}  
Put: /students/{studentId}  
{  
&nbsp;&nbsp;"name": "XXX",  
&nbsp;&nbsp;"imageURL":"XXX",  
&nbsp;&nbsp;"coursesAssisted": [],  
&nbsp;&nbsp;"coursesEnrolled": [],  
&nbsp;&nbsp;"programName": "{programName}"  (set null for an empty vale)  
}  
Delete: /students/{studentId}  

* Professors:  
Get all: /professors  
Get by department and/or year and/or size: /professors?department=XXX&year=XXX&size=XX  
Get one: /professors/{professorId}  
Post: /professors    
{  
&nbsp;&nbsp;"firstName":"XXX",   
&nbsp;&nbsp;"department":"XXXX",  
&nbsp;&nbsp;"joiningDate":1539196909861,  
&nbsp;&nbsp;"coursesTaught":[]  
}  
Put: /professors/{professorId}   
{  
&nbsp;&nbsp;"firstName":"XXX",  
&nbsp;&nbsp;"department":"XXX",  
&nbsp;&nbsp;"joiningDate":1539196909861,  
&nbsp;&nbsp;"coursesTaught":[]  
}  
Delete: /professors/{professorId}  
