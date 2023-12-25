package com.fx.analyzeFx;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/demo")
public class MainController {
  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
  @Autowired private FxTransactionRepository fxTransactionRepository;

  private final WriteToLog logWriter = new WriteToLog();

  @GetMapping(path = "/")
  public @ResponseBody String defaultPage() {
    return "Hello";
  }

  @PostMapping("/requestTransaction") // Map ONLY POST Requests
  public @ResponseBody HashMap<String, Object> requestTransaction(@RequestBody JSONObject data) {
    StringBuilder log = new StringBuilder();
    StringBuilder logException = new StringBuilder();
    log.append(dtf.format(LocalDateTime.now()))
        .append(System.lineSeparator())
        .append("==========================================================")
        .append(System.lineSeparator());

    log.append("Request Data: ").append(System.lineSeparator());
    log.append(data).append(System.lineSeparator());

    logException.append(log.toString());
    HashMap<String, Object> output = new HashMap<>();
    output.put("ErrorCode", 0);
    output.put("ErrorDescription", "Success");

    FxTransaction transaction = new FxTransaction();

    Set<String> missingKeys = new HashSet<>();
    Set<String> formatMismatch = new HashSet<>();
    Set<String> otherExceptions = new HashSet<>();

    try{
      try {
        transaction.setTransactionId((Integer) data.get("transactionId"));
        if (transaction.getTransactionId() == null) {
          output.put("ErrorCode", 1);
          output.put("ErrorDescription", "Error");
          missingKeys.add("transactionId");
        }
      } catch (ClassCastException e) {
        output.put("ErrorCode", 1);
        output.put("ErrorDescription", "Error");
        formatMismatch.add("transactionId");
      }

      try {
        transaction.setCurrencyFrm(Currency.getInstance((String) data.get("currencyFrm")));
      } catch (IllegalArgumentException e) {
        output.put("ErrorCode", 1);
        output.put("ErrorDescription", "Error");
        otherExceptions.add("Unknown From/To Currency");
      } catch (NullPointerException e) {
        output.put("ErrorCode", 1);
        output.put("ErrorDescription", "Error");
        missingKeys.add("currencyFrm");
      } catch (ClassCastException e) {
        output.put("ErrorCode", 1);
        output.put("ErrorDescription", "Error");
        formatMismatch.add("currencyFrm");
      }

      try {
        transaction.setCurrencyTo((Currency.getInstance((String) data.get("currencyTo"))));
      } catch (IllegalArgumentException e) {
        output.put("ErrorCode", 1);
        output.put("ErrorDescription", "Error");
        otherExceptions.add("Unknown From/To Currency");
      } catch (NullPointerException e) {
        output.put("ErrorCode", 1);
        output.put("ErrorDescription", "Error");
        missingKeys.add("currencyFrm");
      } catch (ClassCastException e) {
        output.put("ErrorCode", 1);
        output.put("ErrorDescription", "Error");
        formatMismatch.add("currencyFrm");
      }

      try {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parsedDate = dateFormat.parse((String) data.get("dealDate"));
        transaction.setDealDate(parsedDate);
      } catch (ParseException e) {
        output.put("ErrorCode", 1);
        output.put("ErrorDescription", "Error");
        otherExceptions.add("Unknown Date Format");
      } catch (NullPointerException e) {
        output.put("ErrorCode", 1);
        output.put("ErrorDescription", "Error");
        missingKeys.add("dealDate");
      }

      try {
        transaction.setDealAmount((Double) data.get("dealAmount"));
        if (transaction.getDealAmount() == null) {
          output.put("ErrorCode", 1);
          output.put("ErrorDescription", "Error on data");
          missingKeys.add("dealAmount");
        }
      } catch (ClassCastException e) {
        output.put("ErrorCode", 1);
        output.put("ErrorDescription", "Error");
        formatMismatch.add("dealAmount");
      }

      if (fxTransactionRepository.existsById(transaction.getTransactionId())) {
        output.put("ErrorCode", 1);
        output.put("ErrorDescription", "Error");
        otherExceptions.add("Transaction Cannot be loaded again");
      }

      if ((Integer) output.get("ErrorCode") == 0) {
        fxTransactionRepository.save(transaction);
      } else {
        output.put("MissingJSONElements", missingKeys);
        output.put("FormatMismatchElements", formatMismatch);
        output.put("OtherErrors", otherExceptions);
      }
    } catch (Exception e){
      output.put("ErrorCode", 1);
      output.put("ErrorDescription", "Error");
      logException.append(e.getMessage()).append(System.lineSeparator());
      logException.append(Arrays.toString(e.getStackTrace())).append(System.lineSeparator());
      logException.append("==========================================================")
              .append(System.lineSeparator())
              .append(System.lineSeparator());
      logWriter.writeToExceptionLog(logException.toString());
    }

    log.append("Response Data: ").append(System.lineSeparator());
    log.append(output).append(System.lineSeparator());
    log.append("==========================================================")
        .append(System.lineSeparator())
        .append(System.lineSeparator());

    logWriter.writeLog(log.toString());
    return output;
  }

  @GetMapping(path = "/allTransactions")
  public @ResponseBody Iterable<FxTransaction> allTransactions() {
    String log = dtf.format(LocalDateTime.now()) +
            System.lineSeparator() +
            "==========================================================" +
            System.lineSeparator() +
            "Request Data: " + System.lineSeparator() + "Request All Data" + System.lineSeparator() +
            "==========================================================" +
            System.lineSeparator() +
            System.lineSeparator();

    logWriter.writeLog(log);
    return fxTransactionRepository.findAll();
  }
}
