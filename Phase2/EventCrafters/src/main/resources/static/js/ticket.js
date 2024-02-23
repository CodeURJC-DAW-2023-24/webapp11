window.jsPDF = window.jspdf.jsPDF;
document.getElementById('myguionpdf').onclick= function(){
    /**
     * crear PDF
     */
     var doc = new jsPDF('p', 'pt', 'letter');
     var margin = 10;
     var scale = (doc.internal.pageSize.width - margin * 2) / document.body.clientWidth;
     var scale_mobile = (doc.internal.pageSize.width - margin * 2) / document.body.getBoundingClientRect();
     var blocktxt = document.querySelectorAll('blocktxt');
     
          //true for pc, false for mobile devices
        doc.html(document.querySelector('.contentinput'), {
            x: margin,
            y: margin,
            html2canvas: {
                scale: scale,
            },
            callback: function(doc){
              doc.save('fichero-pdf.pdf');
            }
        });  
 }
