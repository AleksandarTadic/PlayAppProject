import flask
from flask.blueprints import Blueprint

from utils.db import mysql

movie_blueprint = Blueprint("movie_blueprint",__name__)

@movie_blueprint.route("/movie", methods=["GET"])
def get_movies():
    cursor = mysql.get_db().cursor()
    # cursor.execute("SELECT * FROM movie")
    cursor.execute("SELECT * FROM movie ORDER BY id DESC")
    movies = cursor.fetchall()
    return flask.jsonify(movies)

@movie_blueprint.route("/movie/search/<string:title>", methods=["GET"])
def get_movies_by_title(title):
    cursor = mysql.get_db().cursor()
    title = "%" + title + "%"
    cursor.execute("SELECT * FROM movie WHERE title LIKE %s", (title,))
    movies = cursor.fetchall()
    return flask.jsonify(movies)


@movie_blueprint.route("/movie/<int:id>")
def get_movie(id):
    cursor = mysql.get_db().cursor()
    cursor.execute("SELECT * FROM movie WHERE id=%s", (id,))
    movie = cursor.fetchone()
    if movie is not None:
        return flask.jsonify(movie)
    else:
        return "", 404


# Potrebna izmena zbog menjanja baze ali nije potrebno za projekat jer app ne sadrzi dodavanje i izmenu i brisanje

# @movie_blueprint.route("/movie", methods=["POST"])
# def add_movie():
#     db = mysql.get_db()
#     cursor = db.cursor()
#     print(flask.request.json)
#     cursor.execute("INSERT INTO movie(title, video_url, details, image_url) VALUES(%(title)s, %(video_url)s, %(details)s, %(image_url)s)", flask.request.json)
#     db.commit()
#     return flask.jsonify(flask.request.json), 201

# @movie_blueprint.route("/movie/<int:id>", methods=["PUT"])
# def update_movie(id):
#     if flask.session.get("movie") is None:
#         return "", 403
#     db = mysql.get_db()
#     cursor = db.cursor()
#     data = flask.request.json
#     data["id"] = id
#     cursor.execute("UPDATE movie SET title=%(title)s, video_url=%(video_url)s, details=%(details)s, image_url=%(image_url)s WHERE id=%(id)s", data)
#     db.commit()
#     return "", 200
