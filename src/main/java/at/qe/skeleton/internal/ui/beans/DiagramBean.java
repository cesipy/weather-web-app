package at.qe.skeleton.internal.ui.beans;

import at.qe.skeleton.external.model.currentandforecast.misc.holiday.HolidayDTO;
import jakarta.annotation.PostConstruct;

import jakarta.inject.Named;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;

import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.springframework.context.annotation.Scope;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Named
@Scope("view")
public class DiagramBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private LineChartModel lineModel;



    @PostConstruct
    public void init() {
        createLineModel();

    }



    public void createLineModel() {
        lineModel = new LineChartModel();
    }

    public void updateLineModel(List<HolidayDTO> holidays, Date startDate, Date endDate){
        ChartData data = new ChartData();

        LineChartDataSet dataSetMin;
        LineChartDataSet dataSetMax;
        LineChartDataSet dataSetMorn;
        LineChartDataSet dataSetAft;
        LineChartDataSet dataSetEve;
        LineChartDataSet dataSetNight;

        dataSetMin = new LineChartDataSet();
        dataSetMax = new LineChartDataSet();
        dataSetMorn = new LineChartDataSet();
        dataSetAft = new LineChartDataSet();
        dataSetEve = new LineChartDataSet();
        dataSetNight = new LineChartDataSet();


        List<Object> minList = new ArrayList<>();
        List<Object> maxList = new ArrayList<>();
        List<Object> mornList = new ArrayList<>();
        List<Object> afternoonList = new ArrayList<>();
        List<Object> eveList = new ArrayList<>();
        List<Object> nightList = new ArrayList<>();

        for (HolidayDTO holiday : holidays) {
            minList.add(holiday.temperatureDTO().minimumDailyTemperature());
            maxList.add(holiday.temperatureDTO().maximumDailyTemperature());
            mornList.add(holiday.temperatureDTO().morningTemperature());
            afternoonList.add(holiday.temperatureDTO().dayTemperature());
            eveList.add(holiday.temperatureDTO().eveningTemperature());
            nightList.add(holiday.temperatureDTO().nightTemperature());
        }
        dataSetMin.setLabel("Minimum Temperature Evolution");
        dataSetMin.setBorderColor("rgb(75, 192, 192)");
        dataSetMin.setYaxisID("min");
        dataSetMin.setData(minList);

        dataSetMax.setLabel("Maximum Temperature Evolution");
        dataSetMax.setBorderColor("rgb(17, 94, 217)");
        dataSetMax.setYaxisID("min");
        dataSetMax.setData(maxList);

        dataSetMorn.setLabel("Morning Temperature Evolution");
        dataSetMorn.setBorderColor("rgb(191, 21, 152)");
        dataSetMorn.setYaxisID("min");
        dataSetMorn.setData(mornList);

        dataSetAft.setLabel("Afternoon Temperature Evolution");
        dataSetAft.setBorderColor("rgb(92, 122, 37)");
        dataSetAft.setYaxisID("min");
        dataSetAft.setData(afternoonList);

        dataSetEve.setLabel("Evening Temperature Evolution");
        dataSetEve.setBorderColor("rgb(242, 236, 53)");
        dataSetEve.setYaxisID("min");
        dataSetEve.setData(eveList);

        dataSetNight.setLabel("Night Temperature Evolution");
        dataSetNight.setBorderColor("rgb(199, 64, 82)");
        dataSetNight.setYaxisID("min");
        dataSetNight.setData(nightList);

        data.addChartDataSet(dataSetMin);
        data.addChartDataSet(dataSetMax);
        data.addChartDataSet(dataSetMorn);
        data.addChartDataSet(dataSetAft);
        data.addChartDataSet(dataSetEve);
        data.addChartDataSet(dataSetNight);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        List<String> minLabelList = new ArrayList<>();
        while (!start.after(end)) {
            minLabelList.add(sdf.format(start.getTime()));
            start.add(Calendar.DATE, 1);
        }

        data.setLabels(minLabelList);

        LineChartOptions options = new LineChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setId("min");
        linearAxes.setPosition("left");

        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

        lineModel.setOptions(options);
        lineModel.setData(data);
    }
    public LineChartModel getLineModel() {
        return lineModel;
    }

    public void setLineModel(LineChartModel lineModel) {
        this.lineModel = lineModel;
    }
}

