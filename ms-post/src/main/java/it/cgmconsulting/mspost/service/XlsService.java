package it.cgmconsulting.mspost.service;

import it.cgmconsulting.mspost.payload.response.AvgPosts;
import it.cgmconsulting.mspost.payload.response.ReportAuthorResponse;
import it.cgmconsulting.mspost.payload.response.UserPostResponse;
import it.cgmconsulting.mspost.payload.response.UserResponse;
import it.cgmconsulting.mspost.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
@RequiredArgsConstructor
public class XlsService {

    private final PostService postService;

    public InputStream createReport() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // scrittura foglio XLS

        HSSFWorkbook workbook = new HSSFWorkbook();

        // creazione Sheet "Author Report"

        createAuthorReport(workbook);

        workbook.write(out);
        workbook.close();

        InputStream in = new ByteArrayInputStream(out.toByteArray());
        return in;
    }

    public void createAuthorReport (HSSFWorkbook workbook){
        HSSFSheet sheet =  workbook.createSheet("Author Report");

        // userId, username, numero totale di post scritti, media totale

        int rowNum = 0, columnNum = 0;  // cella A1
        Row row; Cell cell;

        row = sheet.createRow(rowNum);
        String[] labels = {"userId", "Username", "TOT posts", "MEDIA"};

        for(String s : labels){
            cell = row.createCell(columnNum++, CellType.STRING);
            cell.setCellValue(s);

            List<UserResponse> writers = postService.getUsersByRole("ROLE_WRITER");
            List<UserPostResponse> list= postService.getUserPostResponses();
            List<AvgPosts> avgPosts = postService.getAllAvg();
            Map<Long, Set<Long>> userPostMap = new HashMap<>();

            for(UserResponse u : writers){
                userPostMap.put(u.getId(),
                        list.stream()
                                .filter(p -> p.getUserId() == u.getId())
                                .map(p -> p.getPostId())
                                .collect(Collectors.toSet()));
            }

            List<ReportAuthorResponse> finalList = new ArrayList<>();

            for(Map.Entry<Long, Set<Long>> map : userPostMap.entrySet()){
                ReportAuthorResponse x = new ReportAuthorResponse(
                        map.getKey(),
                        map.getValue().size(),
                        writers.stream().filter(w -> w.getId() == map.getKey())
                                .map(wr -> wr.getUsername())
                                .toString(),
                        calcolaAvg(map.getValue(), avgPosts)
                );
                finalList.add(x);
            }

            for (ReportAuthorResponse r : finalList){

                columnNum = 0;
                row = sheet.createRow(++rowNum);

                // id
                cell = row.createCell(columnNum++, CellType.NUMERIC);
                cell.setCellValue(r.getUserId());

                //username

                cell = row.createCell(columnNum++, CellType.STRING);
                cell.setCellValue(r.getUsername());

                //TOT posts

                cell = row.createCell(columnNum++, CellType.NUMERIC);
                cell.setCellValue(r.getTotPosts());

                // Media

                cell = row.createCell(columnNum++, CellType.NUMERIC);
                cell.setCellValue(r.getTotAvg());
            }

        }


    }

    public double calcolaAvg (Set<Long> postIds, List<AvgPosts> avgPosts){

        Set<Double> avgs = new HashSet<>();
        for (long l : postIds){
            for(AvgPosts a : avgPosts)
                if (l == a.getPostId())
                    avgs.add(a.getAvg());
        }
        double avg = 0.0d;
       for(Double d : avgs){
           avg =+d;
       }
       if(avgPosts.size() != 0)
           return avg/ avgs.size();
       return 0;

    }


}




