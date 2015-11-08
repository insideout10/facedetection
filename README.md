<a href="http://blog.insideout.io/about-us"><img src="https://github.com/insideout10/facedetection/blob/develop/images/insideout10-mico-opensource.png?raw=true" /></a>

Facedetection for MICO
==============================

## Overview

This tool, developed by [Insideout10](http://blog.insideout.io/about-us/), handles face detection analysis requests to [MICO](http://www.mico-project.eu) 
and to [BetaFaceAPI](http://betafaceapi.com). It also allows an editor to create ground data by annotating manually each face in a set of images.
The tool has been used to asses the accuracy (precision/recall and F1-measure) of the face detection in **FP7 MICO** _Media in Context_. 

The official **MICO** Web Site is [www.mico-project.eu](http://www.mico-project.eu).

<img src="https://github.com/insideout10/facedetection/blob/develop/images/screenshot-facedetection-2015_11_06 19_59.png?raw=true" />

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

## License

The MIT License (MIT)

Copyright (c) 2015 Insideout10

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
