from blueprints.movie import get_movie
import flask
import mimetypes
import os
import re
import threading
from flask import Flask, request, Response
from flask.blueprints import Blueprint

from utils.db import mysql

video_blueprint = Blueprint("video_blueprint",__name__)

def send_file_partial(full_path):
    file_size = os.stat(full_path).st_size
    start = 0
    length = 10240  # can be any default length you want

    range_header = request.headers.get('Range', None)
    if range_header:
        m = re.search('([0-9]+)-([0-9]*)', range_header)  # example: 0-1000 or 1250-
        g = m.groups()
        byte1, byte2 = 0, None
        if g[0]:
            byte1 = int(g[0])
        if g[1]:
            byte2 = int(g[1])
        if byte1 < file_size:
            start = byte1
        if byte2:
            length = byte2 + 1 - byte1
        else:
            length = file_size - start

    with open(full_path, 'rb') as f:
        f.seek(start)
        chunk = f.read(length)

    # rv = Response(chunk, 206, mimetype='video/mp4', content_type='video/mp4', direct_passthrough=True)
    rv = Response(chunk, 206, mimetype=mimetypes.guess_type(full_path), content_type=mimetypes.guess_type(full_path), direct_passthrough=True)
    rv.headers.add('Content-Range', 'bytes {0}-{1}/{2}'.format(start, start + length - 1, file_size))
    return rv

@video_blueprint.route("/video/<int:id>")
def play_video(id):
    cursor = mysql.get_db().cursor()
    cursor.execute("SELECT * FROM movie WHERE id=%s", (id,))
    movie = cursor.fetchone()
    movie = movie["video_url"]
    movie = movie.replace("\\", "/")
    return send_file_partial(movie)

@video_blueprint.after_request
def after_request(response):
    response.headers.add('Accept-Ranges', 'Bytes')
    return response