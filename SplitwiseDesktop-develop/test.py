#!/usr/bin/env python

from http.server import BaseHTTPRequestHandler, HTTPServer
import logging

PORT = 8000

class GetHandler(BaseHTTPRequestHandler):

    def do_GET(self):
        print("GET request,\n Path: ",str(self.path),"\nHeaders :",str(self.headers))
        self.send_response(200)
        self.send_header('Content-type', 'text/html')
        self.end_headers()
        self.wfile.write("Hello World".encode('utf-8'))

    def do_POST(self):
        content_length = int(self.headers['Content-Length'])
        post_data = self.rfile.read(content_length)
        print("POST request,\n Path: ",str(self.path),"\nHeaders :",str(self.headers),"\n Body:",post_data.decode("utf-8"))
        self.send_response(200)
        self.send_header('Content-type', 'text/html')
        self.end_headers()
        self.wfile.write("Hello World".encode('utf-8'))

Handler = GetHandler

httpd = HTTPServer(('',8000),GetHandler)
try:
    httpd.serve_forever()
except KeyboardInterrupt:
    pass
httpd.server_close()
