package org.skyve.impl.web.faces.actions;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.jboss.weld.exceptions.IllegalArgumentException;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.ChartModel;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.donut.DonutChartDataSet;
import org.primefaces.model.charts.donut.DonutChartModel;
import org.primefaces.model.charts.donut.DonutChartOptions;
import org.primefaces.model.charts.hbar.HorizontalBarChartDataSet;
import org.primefaces.model.charts.hbar.HorizontalBarChartModel;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;
import org.primefaces.model.charts.pie.PieChartOptions;
import org.primefaces.model.charts.polar.PolarAreaChartDataSet;
import org.primefaces.model.charts.polar.PolarAreaChartModel;
import org.primefaces.model.charts.polar.PolarAreaChartOptions;
import org.primefaces.model.charts.radar.RadarChartDataSet;
import org.primefaces.model.charts.radar.RadarChartModel;
import org.primefaces.model.charts.radar.RadarChartOptions;
import org.skyve.CORE;
import org.skyve.domain.Bean;
import org.skyve.impl.metadata.view.model.chart.ChartBuilderMetaData;
import org.skyve.impl.metadata.view.widget.Chart.ChartType;
import org.skyve.impl.persistence.AbstractPersistence;
import org.skyve.impl.util.UtilImpl;
import org.skyve.impl.web.faces.FacesAction;
import org.skyve.impl.web.faces.beans.FacesView;
import org.skyve.metadata.customer.Customer;
import org.skyve.metadata.model.document.Document;
import org.skyve.metadata.module.Module;
import org.skyve.metadata.user.User;
import org.skyve.metadata.view.model.chart.MetaDataChartModel;

/**
 * Create a PF chart model from a Skyve model.
 */
public class ChartAction<T extends Bean> extends FacesAction<ChartModel> {
	private FacesView<T> facesView;
	private Object model;
	private ChartType type;
	
	public ChartAction(FacesView<T> facesView, Object model, ChartType type) {
		this.facesView = facesView;
		this.model = model;
		this.type = type;
	}

	@Override
	public ChartModel callback() throws Exception {
		if (UtilImpl.FACES_TRACE) UtilImpl.LOGGER.info("ChartAction - CHART " + model);

		AbstractPersistence persistence = AbstractPersistence.get();
		Bean targetBean = ActionUtil.getTargetBeanForViewAndCollectionBinding(facesView, null, null);
    	User user = persistence.getUser();
    	Customer customer = user.getCustomer();
    	Module targetModule = customer.getModule(targetBean.getBizModule());
		Document targetDocument = targetModule.getDocument(customer, targetBean.getBizDocument());
		org.skyve.metadata.view.model.chart.ChartModel<Bean> chartModel = null;
		if (model instanceof String) {
			chartModel = CORE.getRepository().getChartModel(customer, targetDocument, (String) model, true);
		}
		else {
			chartModel = new MetaDataChartModel((ChartBuilderMetaData) model);
		}
		chartModel.setBean(targetBean);
		org.skyve.metadata.view.model.chart.ChartData data = chartModel.getChartData();

		Title title = title(data);

		ChartModel result = null;
		
		boolean horizontal = ChartType.horizontalBar.equals(type);
		if (ChartType.bar.equals(type) || horizontal) {
			BarChartDataSet set = horizontal ? new HorizontalBarChartDataSet() : new BarChartDataSet();
			set.setData(data.getValues());
			set.setLabel(data.getLabel());
			set.setBackgroundColor(web(data.getBackground()));
			set.setBorderColor(web(data.getBorder()));
			set.setBorderWidth(Integer.valueOf(1));
			ChartData chartData = new ChartData();
			chartData.addChartDataSet(set);
			chartData.setLabels(data.getLabels());
			BarChartModel barChartModel = horizontal ? new HorizontalBarChartModel() : new BarChartModel();
			barChartModel.setData(chartData);
			
			if (title != null) {
				BarChartOptions options = new BarChartOptions();
				options.setTitle(title);
				barChartModel.setOptions(options);
			}

			result = barChartModel;
		}
		else if (ChartType.doughnut.equals(type)) {
			DonutChartDataSet set = new DonutChartDataSet();
			set.setData(data.getValues());
			set.setBackgroundColor(web(data.getBackgrounds()));
			set.setBorderColor(web(data.getBorders()));
			ChartData chartData = new ChartData();
			chartData.addChartDataSet(set);
			chartData.setLabels(data.getLabels());
			DonutChartModel donutChartModel = new DonutChartModel();
			donutChartModel.setData(chartData);
			
			if (title != null) {
				DonutChartOptions options = new DonutChartOptions();
				options.setTitle(title);
				donutChartModel.setOptions(options);
			}

			result = donutChartModel;
		}
		else if (ChartType.line.equals(type) || ChartType.lineArea.equals(type)) {
			LineChartDataSet set = new LineChartDataSet();
			set.setData(data.getValues());
			set.setLabel(data.getLabel());
			set.setBackgroundColor(web(data.getBackground()));
			set.setBorderColor(web(data.getBorder()));
			set.setPointBackgroundColor(set.getBorderColor());
			set.setPointBorderColor(set.getBorderColor());
			set.setBorderWidth(Integer.valueOf(1));
			if (ChartType.line.equals(type)) {
				set.setFill(Boolean.FALSE);
			}
			ChartData chartData = new ChartData();
			chartData.addChartDataSet(set);
			chartData.setLabels(data.getLabels());
			LineChartModel lineChartModel = new LineChartModel();
			lineChartModel.setData(chartData);
			
			if (title != null) {
				LineChartOptions options = new LineChartOptions();
				options.setTitle(title);
				lineChartModel.setOptions(options);
			}

			result = lineChartModel;
		}
		else if (ChartType.pie.equals(type)) {
			PieChartDataSet set = new PieChartDataSet();
			set.setData(data.getValues());
			set.setBackgroundColor(web(data.getBackgrounds()));
			set.setBorderColor(web(data.getBorders()));
			ChartData chartData = new ChartData();
			chartData.addChartDataSet(set);
			chartData.setLabels(data.getLabels());
			PieChartModel pieChartModel = new PieChartModel();
			pieChartModel.setData(chartData);
			
			if (title != null) {
				PieChartOptions options = new PieChartOptions();
				options.setTitle(title);
				pieChartModel.setOptions(options);
			}

			result = pieChartModel;
		}
		else if (ChartType.polarArea.equals(type)) {
			PolarAreaChartDataSet set = new PolarAreaChartDataSet();
			set.setData(data.getValues());
			set.setBackgroundColor(web(data.getBackgrounds()));
			set.setBorderColor(web(data.getBorders()));
			ChartData chartData = new ChartData();
			chartData.addChartDataSet(set);
			chartData.setLabels(data.getLabels());
			PolarAreaChartModel polarChartModel = new PolarAreaChartModel();
			polarChartModel.setData(chartData);
			
			if (title != null) {
				PolarAreaChartOptions options = new PolarAreaChartOptions();
				options.setTitle(title);
				polarChartModel.setOptions(options);
			}
			
			result = polarChartModel;
		}
		else if (ChartType.radar.equals(type)) {
			RadarChartDataSet set = new RadarChartDataSet();
			set.setData(data.getValues());
			set.setLabel(data.getLabel());
			set.setBackgroundColor(web(data.getBackground()));
			set.setBorderColor(web(data.getBorder()));
			set.setBorderWidth(Integer.valueOf(1));
			set.setPointBackgroundColor(set.getBorderColor());
			set.setPointBorderColor(set.getBorderColor());
			ChartData chartData = new ChartData();
			chartData.addChartDataSet(set);
			chartData.setLabels(data.getLabels());
			RadarChartModel radarChartModel = new RadarChartModel();
			radarChartModel.setData(chartData);

			if (title != null) {
				RadarChartOptions options = new RadarChartOptions();
				options.setTitle(title);
				radarChartModel.setOptions(options);
			}

			result = radarChartModel;
		}
		else {
			throw new IllegalArgumentException("Chart Type " + type + " is not supported.");
		}
		return result;
	}
	
	private static Title title(org.skyve.metadata.view.model.chart.ChartData data) {
		Title result = null;
		String text = data.getTitle();
		if (text != null) {
			result = new Title();
			result.setDisplay(true);
			result.setText(text);
		}
		return result;
	}
	
	private static String web(Color colour) {
		if (colour == null) {
			return null;
		}
		int alpha = colour.getAlpha();
		if (alpha > 0) {
			return new StringBuilder(16).append("rgba(").append(colour.getRed()).append(',').append(colour.getGreen()).append(',').append(colour.getBlue()).append(',').append(((alpha / 255.0) * 100) / 100.0).append(')').toString();
		}
		return new StringBuilder(16).append("rgb(").append(colour.getRed()).append(',').append(colour.getGreen()).append(',').append(colour.getBlue()).append(')').toString();
	}
	
	private static List<String> web(List<Color> colours) {
		if (colours == null) {
			return null;
		}
		List<String> result = new ArrayList<>(colours.size());
		for (Color colour : colours) {
			result.add(web(colour));
		}
		return result;
	}
}