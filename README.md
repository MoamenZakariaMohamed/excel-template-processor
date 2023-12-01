# excel-template-processor
Allows processing Excel files uploaded via API and download populate templated Excel 


Key Functionality:
- Upload Excel data file through API
- Map columns from uploaded file into DTO 
- Applies formatting and styling from template Excel
- Generates new populated Excel report
- Persistence using postgre database

Technical Details:  
- Spring Boot web application 
- Apache POI reads, parses, and writes Excel files
- Supports XLS formats
- Dynamic cell formatting and styling 
- Database stores uploaded file data
- Clear layered architecture


Takeaways: 
- Production-grade foundations for Excel generation web application
- Handles large datasets without memory issues
- Best practices for Excel file processing with Java

