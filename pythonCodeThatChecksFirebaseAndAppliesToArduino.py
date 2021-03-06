import serial
from time import sleep
from time import localtime
from time import strftime
from pynput import keyboard
from threading import Thread
import firebase_admin
from firebase_admin import credentials
from firebase_admin import db

ser = serial.Serial('COM8', 9600)
on = '1'
off = '0'
isturnedoff = True
halt = False


# Fetch the service account key JSON file contents
cred = credentials.Certificate('cleverroom-yeet-firebase-adminsdk-d6q4g-6df49f7c01.json')
# Initialize the app with a service account, granting admin privileges
firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://cleverroom-yeet.firebaseio.com/'
})

computersidereference = db.reference('/controlpanel/computerside/')
appsidereference = db.reference('/controlpanel/appside/')

def updateComputerSide():
    global computersidereference
    t = localtime()
    current_time = strftime("%d/%m/%Y %H:%M:%S", t)

    try:
        if isturnedoff:
            computersidereference.set({
                    'state': "off",
                    'lastseen': str(current_time)
                })
        else:
            computersidereference.set({
                    'state': "on",
                    'lastseen': str(current_time)
                })
    except:
        try:
            # Fetch the service account key JSON file contents
            cred = credentials.Certificate('cleverroom-yeet-firebase-adminsdk-d6q4g-6df49f7c01.json')
            # Initialize the app with a service account, granting admin privileges
            firebase_admin.initialize_app(cred, {
                'databaseURL': 'https://cleverroom-yeet.firebaseio.com/'
            })
            computersidereference = db.reference('/controlpanel/computerside/')
            appsidereference = db.reference('/controlpanel/appside/')
        except:
            pass

def checkAppSide():
    global appsidereference
    global isturnedoff
    global ser
    try:
        mostrecentrequest = appsidereference.child("mostrecentrequest").get()
        try:
            if str(mostrecentrequest)=="on":
                isturnedoff = False
                ser.write(on.encode())
            elif str(mostrecentrequest)=="off":
                isturnedoff = True
                ser.write(off.encode())
        except:
            ser.close()
            ser = serial.Serial('COM8', 9600)
            sleep(2)
            print("failed to get info from Firebase checkAppSide()")
    except:
        try:
            # Fetch the service account key JSON file contents
            cred = credentials.Certificate('cleverroom-yeet-firebase-adminsdk-d6q4g-6df49f7c01.json')
            # Initialize the app with a service account, granting admin privileges
            firebase_admin.initialize_app(cred, {
                'databaseURL': 'https://cleverroom-yeet.firebaseio.com/'
            })
            computersidereference = db.reference('/controlpanel/computerside/')
            appsidereference = db.reference('/controlpanel/appside/')
        except:
            pass


def FirebaseUpdater():
    global halt
    while halt==False:
        sleep(0.5)
        checkAppSide()
        updateComputerSide()

thread = Thread(target = FirebaseUpdater)
thread.start()



# keyboard things
def on_press(key):
    global isturnedoff
    global ser
    if str(key).find("end")>-1:
        if isturnedoff:
            try:
                appsidereference = db.reference('/controlpanel/appside/')
                appsidereference.set({
                        'mostrecentrequest': "on"
                    })
                isturnedoff = False
                try:
                    ser.write(off.encode())
                except:
                    ser.close()
                    ser = serial.Serial('COM8', 9600)
                    sleep(2)
            except:
                try:
                    # Fetch the service account key JSON file contents
                    cred = credentials.Certificate('cleverroom-yeet-firebase-adminsdk-d6q4g-6df49f7c01.json')
                    # Initialize the app with a service account, granting admin privileges
                    firebase_admin.initialize_app(cred, {
                        'databaseURL': 'https://cleverroom-yeet.firebaseio.com/'
                    })
                    computersidereference = db.reference('/controlpanel/computerside/')
                    appsidereference = db.reference('/controlpanel/appside/')
                except:
                    pass
        else:
            try:
                appsidereference = db.reference('/controlpanel/appside/')
                appsidereference.set({
                        'mostrecentrequest': "off"
                    })
                isturnedoff = True
                try:
                    ser.write(off.encode())
                except:
                    ser.close()
                    ser = serial.Serial('COM8', 9600)
                    sleep(2)
            except:
                try:
                    # Fetch the service account key JSON file contents
                    cred = credentials.Certificate('cleverroom-yeet-firebase-adminsdk-d6q4g-6df49f7c01.json')
                    # Initialize the app with a service account, granting admin privileges
                    firebase_admin.initialize_app(cred, {
                        'databaseURL': 'https://cleverroom-yeet.firebaseio.com/'
                    })
                    computersidereference = db.reference('/controlpanel/computerside/')
                    appsidereference = db.reference('/controlpanel/appside/')
                except:
                    pass

def on_release(key):
    global halt
    if key == keyboard.Key.esc: # REMOVE ME REMOVE ME REMOVE ME REMOVE ME REMOVE ME REMOVE ME REMOVE ME REMOVE ME REMOVE ME REMOVE ME REMOVE ME REMOVE ME REMOVE ME REMOVE ME REMOVE ME REMOVE ME REMOVE ME REMOVE ME REMOVE ME
        pass
	#    halt = True
    #    return False

with keyboard.Listener(on_press=on_press,on_release=on_release) as listener:listener.join()
listener = keyboard.Listener(on_press=on_press,on_release=on_release)
listener.start()
