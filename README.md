<a href="http://blog.insideout.io/about-us"><img src="https://insideout10.github.io/facedetection/images/insideout10-opensource.png" /></a>

Facedetection for MICO
==============================

## Overview

This tool, developed by [Insideout10](http://blog.insideout.io/about-us/), handles face detection analysis requests to [MICO](http://www.mico-project.eu) 
and to [BetaFaceAPI](http://betafaceapi.com). It also allows an editor to create ground data by annotating manually each face in a set of images.
The tool has been used to asses the accuracy (precision/recall and F1-measure) of the face detection in **FP7 MICO** _Media in Context_. 

The official The **MICO** Official Web Site is [www.mico-project.eu](http://www.mico-project.eu).

## Description

This tool has been designed with the purpose of executing the following tasks: 

1. upload the dataset of images
2. manually annotate the images using a rectangular-shaped selector - the color of the manual annotation is blue
3. sending each image to BetaFaceAPI and to MICO
4. handle exceptions from MICO and retry sending the image after 3 seconds
5. receive the analysis results from BetaFaceAPI and add a red annotation on each image  
6. receive the analysis results from MICO and add a green annotation on each image
7. store all corresponding media fragments as separate image files
8. store the coordinates of each annotation (manual, BetaFaceAPI and MICO) 
9. calculate overlaps, KPI test results and export it to CSV

The tool uses:
- [backbone.js](http://www.backbone.js) for the manual annotations 
- [Apache Camel](http://camel.apache.org) for handling the analysis over to BetaFaceAPI and MICO
- HATEOAS (Hypermedia as the Engine of Application State) to navigate the REST APIs 
