import os
import requests
import datetime
import json
import discord
from discord.ext import commands
from dotenv import load_dotenv
import pyrebase

# .env
load_dotenv()

# firebase
config = {
    'apiKey': os.getenv('apiKey'),
    'authDomain': os.getenv('authDomain'),
    'databaseURL': os.getenv('databaseURL'),
    'storageBucket': os.getenv('storageBucket')
}

firebase = pyrebase.initialize_app(config)
storage = firebase.storage()

# discord
client = commands.Bot(command_prefix='!')


@client.event
async def on_ready():
    print('Ready')


@client.command(name='createuser')
async def _createuser(ctx, arg):
    response = requests.post('http://api.tap.shmn7iii.net/users',
                                json.dumps({"uid": arg}),
                                headers={'Content-Type': 'application/json'}
                            )
    await ctx.channel.send(response.json())


@client.command(name='issue')
async def _issue(ctx, arg):
    uid = arg
    fileurl = ctx.message.attachments[0].url
    date = datetime.datetime.now().strftime("%Y%m%d%H%M%S%f")

    filename = f"{uid}_{date}.png"
    filepath = os.getenv('ROOTPATH') + '/images/' + filename

    # download image from message
    r = requests.get(fileurl, stream=True)
    if r.status_code == 200:
        with open(filepath, 'wb') as f:
            f.write(r.content)

    # upload image to firebase storage
    storage.child('images/' + filename).put(filepath)

    # issue token via API server
    dataurl = "gs://tap-f4f38.appspot.com/images/" + filename
    body = {
            "uid": uid,
            "data": dataurl
    }
    response = requests.post('http://api.tap.shmn7iii.net/tokens',
                                json.dumps(body),
                                headers={'Content-Type': 'application/json'}
                            )

    await ctx.channel.send(response.json())



def download_img(_url, _filepath):
    r = requests.get(_url, stream=True)
    if r.status_code == 200:
        with open(_filepath, 'wb') as f:
            f.write(r.content)


def upload_firebase(_filepath, _filename):
    storage.child('images/' + _filename).put(_filepath)

client.run(os.getenv('TOKEN'))
