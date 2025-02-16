package com.gymmanager.services;

import com.gymmanager.dto.SubscriptionDto;
import com.gymmanager.model.Subscription;
import com.gymmanager.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionExportService {
    private final SubscriptionRepository subscriptionRepository;

    public byte[] exportSubscriptionsToCsv() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
        CSVPrinter csvPrinter = null;
        try {
            csvPrinter = new CSVPrinter(pw, CSVFormat.DEFAULT.withHeader("ID", "Customer ID", "Customer Name", "Pack ID", "Pack Name", "Start Date", "End Date", "Active"));

            // Récupérer tous les abonnements
            List<Subscription> subscriptions = subscriptionRepository.findAll();

            // Écrire les données des abonnements dans le CSV
            for (Subscription subscription : subscriptions) {
                csvPrinter.printRecord(
                    subscription.getId(),
                    subscription.getCustomer().getId(),
                    subscription.getCustomer().getFirstName() + " " + subscription.getCustomer().getLastName(),
                    subscription.getPack().getId(),
                    subscription.getPack().getName(),
                    subscription.getStartDate(),
                    subscription.calculateEndDate(),
                    subscription.isActive()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (csvPrinter != null) {
                try {
                    csvPrinter.flush();
                    csvPrinter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return out.toByteArray();
    }
}