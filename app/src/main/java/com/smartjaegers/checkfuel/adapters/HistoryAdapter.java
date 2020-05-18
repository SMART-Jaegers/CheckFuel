package com.smartjaegers.checkfuel.adapters;

import android.content.Context;
import android.os.Build;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.smartjaegers.checkfuel.R;
import com.smartjaegers.checkfuel.managers.UtilsManagerForStatistic;
import com.smartjaegers.checkfuel.models.DayOfUse;
import com.smartjaegers.checkfuel.models.ItemStatistic;
import com.smartjaegers.checkfuel.models.ViewOfItemStatistic;
import com.smartjaegers.checkfuel.models.Refill;
import com.smartjaegers.checkfuel.models.SortType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<ViewOfItemStatistic> {
    private static final SimpleDateFormat pattern = new SimpleDateFormat("yyyy-MM-dd");
    private List<ItemStatistic> itemStatisticList;
    private Context context;


    @RequiresApi(api = Build.VERSION_CODES.N)
    public HistoryAdapter(List<Refill> refills, List<DayOfUse> daysOfUse, Context context) {
        this.itemStatisticList = createStatisticItems(refills, daysOfUse);
        this.context = context;
    }

    private List<ItemStatistic> createStatisticItems(List<Refill> refills, List<DayOfUse> daysOfUse) {
        List<ItemStatistic> itemStatisticList = new LinkedList<>();
        Collections.reverse(refills);


        try {
            for (int position = 0; position < refills.size(); position++) {
                Date dateOfRefill = pattern.parse(refills.get(position).getDate());
                Date currentDate = Calendar.getInstance().getTime();
                String timeDriving = String.valueOf((currentDate.getTime() - dateOfRefill.getTime()) / (24 * 60 * 60 * 1000));
                Date dateNextRefill = null;
                if (position != 0) {
                    dateNextRefill = pattern.parse(refills.get(position - 1).getDate());
                    timeDriving = String.valueOf((dateNextRefill.getTime() - dateOfRefill.getTime()) / (24 * 60 * 60 * 1000));
                }

                double distance = calculateDistance(daysOfUse, dateOfRefill, dateNextRefill);
                double volume = calculateVolume(daysOfUse, dateOfRefill, dateNextRefill);


                itemStatisticList.add(new ItemStatistic(refills.get(position), distance, volume, timeDriving));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return itemStatisticList;
    }

    private double calculateDistance(List<DayOfUse> daysOfUse, Date refillDate, Date nextRefillDate) {
        double distance = 0;
        try {
            for (DayOfUse dayOfUse : daysOfUse) {
                Date date = null;

                date = pattern.parse(dayOfUse.getDate());
                assert date != null;
                if (nextRefillDate == null) {
                    if ((date.after(refillDate) || date.equals(refillDate))) {
                        distance += dayOfUse.getKmPerDay();
                    }
                } else {
                    if ((date.before(nextRefillDate)) && (date.after(refillDate) || date.equals(refillDate))) {
                        distance += dayOfUse.getKmPerDay();
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return distance;
    }

    private double calculateVolume(List<DayOfUse> daysOfUse, Date refillDate, Date nextRefillDate) {
        double volume = 0;
        try {
            for (DayOfUse dayOfUse : daysOfUse) {
                Date date = null;

                date = pattern.parse(dayOfUse.getDate());
                assert date != null;
                if (nextRefillDate == null) {
                    if ((date.after(refillDate) || date.equals(refillDate))) {
                        volume += dayOfUse.getVolumePerDay();
                    }
                } else {
                    if ((date.before(nextRefillDate)) && (date.after(refillDate) || date.equals(refillDate))) {
                        volume += dayOfUse.getVolumePerDay();
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return volume;
    }

    @NonNull
    @Override
    public ViewOfItemStatistic onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewOfItemStatistic(parent, context);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewOfItemStatistic holder, int position) {

        holder.bind(itemStatisticList.get(position));

    }

    @Override
    public int getItemCount() {
        return itemStatisticList.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setSortingBy(String sortBy) {
        switch (sortBy) {
            case "date_new_old":
                UtilsManagerForStatistic.sortByDate(itemStatisticList, SortType.ASCENDING);
                break;
            case "date_old_new":
                UtilsManagerForStatistic.sortByDate(itemStatisticList, SortType.DESCENDING);
                break;
            case "volume_descending":
                UtilsManagerForStatistic.sortByVolume(itemStatisticList, SortType.DESCENDING);
                break;
            case "volume_ascending":
                UtilsManagerForStatistic.sortByVolume(itemStatisticList, SortType.ASCENDING);
                break;
            case "efficiency_ascending":
                UtilsManagerForStatistic.sortByDistance(itemStatisticList, SortType.ASCENDING);
                break;
            case "efficiency_descending":
                UtilsManagerForStatistic.sortByDistance(itemStatisticList, SortType.DESCENDING);
                break;
        }

    }

}
