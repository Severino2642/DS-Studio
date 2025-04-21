<%@ page import="entiter.DSAudio" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="outils.AudioAnalyzer" %>
<%@ page import="java.util.Collections" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  DSAudio audio = null;
  Gson gson = new Gson();
  List<String> labels = new ArrayList<String>();
  List<Integer> amplitude = new ArrayList<Integer>();
  List<String> labels2 = new ArrayList<String>();
  List<Integer> amplitude2 = new ArrayList<Integer>();

  if (request.getAttribute("audio")!=null){
    audio = (DSAudio)request.getAttribute("audio");
    String originalFilePath = (String) request.getAttribute("path");
    AudioAnalyzer.updateDetailsForChart2(audio.getPath(), labels,amplitude,audio.getAmplitudes());
    AudioAnalyzer.updateDetailsForChart(originalFilePath, labels2,amplitude2);
  }
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, shrink-to-fit=no" name="viewport">
  <title>DS_Studio</title>

  <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/app.min.css">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/bundles/select2/dist/css/select2.min.css">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/bundles/jquery-selectric/selectric.css">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/bundles/bootstrap-tagsinput/dist/bootstrap-tagsinput.css">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/bundles/pretty-checkbox/pretty-checkbox.min.css">
  <!-- Template CSS -->
  <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/style.css">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/components.css">
  <!-- Custom style CSS -->
  <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/custom.css">
  <link rel='shortcut icon' type='image/x-icon' href='<%=request.getContextPath()%>/assets/img/mozart.png' />
</head>
<body>
<div class="loader"></div>
<div id="app">
  <div class="main-wrapper main-wrapper-1">
    <jsp:include page="/navigation.jsp"/>
    <!-- Main Content -->
    <div class="main-content">
      <section class="section">
        <div class="section-body">
          <div class="row">
            <div class="col-12 col-md-3 col-lg-3">
              <div class="card">
                <div class="card-header">
                  <h4>Modifier un son</h4>
                </div>
                <div class="card-body">
                  <ul class="nav nav-tabs" id="myTab1" role="tablist">
                    <li class="nav-item">
                      <a class="nav-link active" id="amplification-tab" data-toggle="tab" href="#amplification" role="tab"
                         aria-controls="amplification" aria-selected="true">Amplification</a>
                    </li>
                    <li class="nav-item">
                      <a class="nav-link" id="antibruit-tab" data-toggle="tab" href="#antibruit" role="tab"
                         aria-controls="antibruit" aria-selected="false">Anti-Bruit</a>
                    </li>
                  </ul>
                  <div class="tab-content tab-bordered" id="myTabContent1">
                    <div class="tab-pane fade show active" id="amplification" role="tabpanel" aria-labelledby="amplification-tab">
                      <form action="<%=request.getContextPath()%>/ModifSound.MainController" method="post" enctype="multipart/form-data">
                        <div class="form-group">
                          <label>Fichier audio (.wav)</label>
                          <div class="input-group">
                            <div class="custom-file">
                              <input type="file" class="custom-file-input" id="customFile" name="audio" accept="audio/*" required>
                              <label class="custom-file-label" for="customFile">Televerser</label>
                            </div>
                          </div>
                        </div>
                        <div class="form-group">
                          <label>Taux d'amplification</label>
                          <div class="input-group">
                            <div class="input-group-prepend" >
                              <div class="input-group-text">
                                <i class="fas fa-sort-numeric-up"></i>
                              </div>
                            </div>
                            <input class="form-control" type="number" step="0.01" name="amplification" >
                          </div>
                        </div>
                        <div class="form-group">
                          <label class="d-block">Anti-Distorsion</label>
                          <div class="pretty p-icon p-round p-plain p-smooth">
                            <input type="radio" name="antiDistortion" value="true">
                            <div class="state p-primary-o">
                              <i class="icon material-icons col-green">done</i>
                              <label>Oui</label>
                            </div>
                          </div>
                          <div class="pretty p-icon p-round p-plain p-smooth">
                            <input type="radio" name="antiDistortion" value="false">
                            <div class="state p-primary-o">
                              <i class="icon material-icons col-red">clear</i>
                              <label>Non</label>
                            </div>
                          </div>
                        </div>
                        <button type="submit" class="btn btn-primary w-100">valider</button>
                      </form>
                    </div>
                    <div class="tab-pane fade" id="antibruit" role="tabpanel" aria-labelledby="antibruit-tab">
                      <form action="<%=request.getContextPath()%>/ModifSound2.MainController" method="post" enctype="multipart/form-data">
                        <div class="form-group">
                          <label>Fichier audio (.wav)</label>
                          <div class="input-group">
                            <div class="custom-file">
                              <input type="file" class="custom-file-input" id="customFile" name="audio" accept="audio/*" required>
                              <label class="custom-file-label" for="customFile">Televerser</label>
                            </div>
                          </div>
                        </div>
                        <div class="form-group">
                          <label>Fichier audio pour le bruit (.wav)</label>
                          <div class="input-group">
                            <div class="custom-file">
                              <input type="file" class="custom-file-input" id="customFile2" name="noiseSound" accept="audio/*">
                              <label class="custom-file-label" for="customFile2">Televerser</label>
                            </div>
                          </div>
                        </div>
                        <button type="submit" class="btn btn-primary w-100">valider</button>
                      </form>

                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-12 col-md-9 col-lg-9">
              <div class="card">
                <ul class="nav nav-tabs" id="myTab" role="tablist">
                  <li class="nav-item">
                    <a class="nav-link active" id="input-tab" data-toggle="tab" href="#input" role="tab"
                       aria-controls="input" aria-selected="true">Audio Original</a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" id="output-tab" data-toggle="tab" href="#output" role="tab"
                       aria-controls="output" aria-selected="false">Audio Modifier</a>
                  </li>
                </ul>
                <div class="tab-content tab-bordered" id="myTabContent">
                  <div class="tab-pane fade show active" id="input" role="tabpanel" aria-labelledby="input-tab">
                    <div class="card-header">
                      <h4>Analyse d'Amplitude</h4>
                    </div>
                    <div class="card-body">
                      <canvas id="line-chart3"></canvas>
                    </div>
                    <% if (audio!=null) { %>
                    <div class="card-header d-flex justify-content-between align-center">
                      <h4>Studio</h4>
                    </div>
                    <div class="card-body">
                      <audio type="audio/<%=audio.getExtension()%>" class="w-100" controls>
                        <source src="<%=request.getContextPath()%>/upload/input.wav">
                      </audio>
                    </div>
                    <% } %>
                  </div>
                  <div class="tab-pane fade" id="output" role="tabpanel" aria-labelledby="output-tab">
                    <div class="card-header">
                      <h4>Analyse d'Amplitude</h4>
                    </div>
                    <div class="card-body">
                      <canvas id="line-chart4"></canvas>
                    </div>
                    <% if (audio!=null) { %>
                    <div class="card-header d-flex justify-content-between align-center">
                      <h4>Studio</h4>
                      <a href="<%=request.getContextPath()%>/upload/<%=audio.getName()%>" download="<%=audio.getName()%>" class="btn btn-success btn-icon icon-left"><i class="fas fa-cloud-download-alt"></i> Telecharger</a>
                    </div>
                    <div class="card-body">
                      <audio type="audio/<%=audio.getExtension()%>" class="w-100" controls>
                        <source src="<%=request.getContextPath()%>/upload/output.wav">
                      </audio>
                    </div>
                    <% } %>
                  </div>
                </div>

              </div>
            </div>
          </div>
        </div>
      </section>
      <jsp:include page="/setting.jsp"/>
    </div>
    <jsp:include page="/footer.jsp"/>
  </div>
</div>

<!-- General JS Scripts -->
<script src="<%=request.getContextPath()%>/assets/js/app.min.js"></script>
<!-- JS Libraies -->
<script src="<%=request.getContextPath()%>/assets/bundles/chartjs/chart.min.js"></script>
<script src="<%=request.getContextPath()%>/assets/bundles/upload-preview/assets/js/jquery.uploadPreview.min.js"></script>
<script src="<%=request.getContextPath()%>/assets/bundles/select2/dist/js/select2.full.min.js"></script>
<script src="<%=request.getContextPath()%>/assets/bundles/summernote/summernote-bs4.js"></script>
<script src="<%=request.getContextPath()%>/assets/bundles/jquery-selectric/jquery.selectric.min.js"></script>
<script src="<%=request.getContextPath()%>/assets/bundles/bootstrap-tagsinput/dist/bootstrap-tagsinput.min.js"></script>
<!-- Page Specific JS File -->
<script src="<%=request.getContextPath()%>/assets/js/page/create-post.js"></script>
<script src="<%=request.getContextPath()%>/assets/js/page/forms-advanced-forms.js"></script>
<!-- Template JS File -->
<script src="<%=request.getContextPath()%>/assets/js/scripts.js"></script>
<!-- Custom JS File -->
<script src="<%=request.getContextPath()%>/assets/js/custom.js"></script>

<script type="text/javascript">
  "use strict";


  var draw = Chart.controllers.line.prototype.draw;
  Chart.controllers.lineShadow = Chart.controllers.line.extend({
    draw: function () {
      draw.apply(this, arguments);
      var ctx = this.chart.chart.ctx;
      var _stroke = ctx.stroke;
      ctx.stroke = function () {
        ctx.save();
        ctx.shadowColor = '#00000075';
        ctx.shadowBlur = 10;
        ctx.shadowOffsetX = 8;
        ctx.shadowOffsetY = 8;
        _stroke.apply(this, arguments)
        ctx.restore();
      }
    }
  });

  function updateChart(nameChart,labels,data){
    var ctx = document.getElementById(nameChart).getContext("2d");


    var gradientStroke = ctx.createLinearGradient(500, 0, 0, 0);
    gradientStroke.addColorStop(0, 'rgba(155, 89, 182, 1)');
    gradientStroke.addColorStop(1, 'rgba(231, 76, 60, 1)');


    let datasets = [{
      label: "Amplitude",
      data: data,
      borderColor: gradientStroke,
      pointBorderColor: gradientStroke,
      pointBackgroundColor: gradientStroke,
      pointHoverBackgroundColor: gradientStroke,
      pointHoverBorderColor: gradientStroke,
      pointBorderWidth: 10,
      pointHoverRadius: 10,
      pointHoverBorderWidth: 1,
      pointRadius: 1,
      fill: false,
      borderWidth: 4,
    }]
    var myChart = new Chart(ctx, {
      type: 'lineShadow',
      data: {
        labels: labels,
        type: 'line',
        defaultFontFamily: 'Poppins',
        datasets: datasets
      },
      options: {
        legend: {
          position: "bottom"
        },
        tooltips: {
          mode: 'index',
          titleFontSize: 12,
          titleFontColor: '#fff',
          bodyFontColor: '#fff',
          backgroundColor: '#000',
          titleFontFamily: 'Poppins',
          bodyFontFamily: 'Poppins',
          cornerRadius: 3,
          intersect: false,
        },
        scales: {
          yAxes: [{
            ticks: {
              fontColor: "#9aa0ac", // Font Color
              fontStyle: "bold",
              beginAtZero: true,
              maxTicksLimit: 5,
              padding: 20
            },
            gridLines: {
              drawTicks: false,
              display: false
            }

          }],
          xAxes: [{
            gridLines: {
              zeroLineColor: "transparent"
            },
            ticks: {
              padding: 20,
              fontColor: "#9aa0ac", // Font Color
              fontStyle: "bold"
            }
          }]
        }
      }
    });
  }
  updateChart('line-chart3',<%=gson.toJson(labels2)%>,<%=gson.toJson(amplitude2)%>)
  updateChart('line-chart4',<%=gson.toJson(labels)%>,<%=gson.toJson(amplitude)%>)
</script>
</body>
</html>
