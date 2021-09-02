import flask
import MySQLdb
from flask.blueprints import Blueprint
import pymysql
from werkzeug.wrappers import response

from utils.db import mysql

account_blueprint = Blueprint("account_blueprint",__name__)

@account_blueprint.route("/account", methods=["GET"])
def get_accounts():
    cursor = mysql.get_db().cursor()
    cursor.execute("SELECT * FROM account")
    accounts = cursor.fetchall()
    return flask.jsonify(accounts)

@account_blueprint.route("/account/<int:id>")
def get_account(id):
    cursor = mysql.get_db().cursor()
    cursor.execute("SELECT * FROM account WHERE id=%s", (id,))
    account = cursor.fetchone()
    if account is not None:
        return flask.jsonify(account)
    else:
        return "", 404

@account_blueprint.route("/account/android/<string:email>")
def get_accountByEmail(email):
    cursor = mysql.get_db().cursor()
    cursor.execute("SELECT * FROM account WHERE email=%s", (email,))
    account = cursor.fetchone()
    if account is not None:
        return flask.jsonify(account)
    else:
        return "", 404

@account_blueprint.route("/account", methods=["POST"])
def add_account():
    db = mysql.get_db()
    cursor = db.cursor()
    try:
        cursor.execute("INSERT INTO account (username, email, password, active) VALUES (%(username)s, %(email)s, %(password)s, %(active)s)", flask.request.json)
    except pymysql.err.IntegrityError:
        return flask.jsonify("Email already exist!"), 306
    db.commit()
    return flask.jsonify(flask.request.json), 201

@account_blueprint.route("/account/<int:id>", methods=["PUT"])
def update_account(id):
    # if flask.session.get("account") is None:
    #     return "", 403
    db = mysql.get_db()
    cursor = db.cursor()
    data = flask.request.json
    data["id"] = id
    try:
        cursor.execute("UPDATE account SET username=%(username)s, email=%(email)s, password=%(password)s, active=%(active)s WHERE id=%(id)s", data)
    except pymysql.err.IntegrityError:
        return flask.jsonify("Email already exist!"), 306
    db.commit()
    return "", 200

@account_blueprint.route("/account/<int:id>", methods=["DELETE"])
def remove_account(id):
    # if flask.session.get("account") is None:
    #     return "", 403
    db = mysql.get_db()
    cursor = db.cursor()
    cursor.execute("UPDATE account SET active=0 WHERE id=%s", (id,))
    db.commit()
    return "", 204

@account_blueprint.route("/login", methods=["POST"])
def login():
    cursor = mysql.get_db().cursor()
    cursor.execute("SELECT * FROM account WHERE email=%(email)s AND password=%(password)s AND active=1", flask.request.json)
    account = cursor.fetchone()
    if account is not None:
        flask.session["account"] = account
        return flask.jsonify(account), 200
    else:
        return flask.jsonify("Email or Password incorrect, or account may be inactive, try again!"), 404



@account_blueprint.route("/logout", methods=["GET"])
def logout():
    flask.session.pop("account", None)
    return "", 200

# Funkcija kojom se dobavlja trenutno ulogovani korisnik.
# Moze se prosiriti da dobavi sve podatke korisnik iz baze podataka.
@account_blueprint.route("/currentUser", methods=["GET"])
def current_account():
    return flask.jsonify(flask.session.get("account")), 200