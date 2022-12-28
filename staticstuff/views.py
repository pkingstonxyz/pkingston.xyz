from django.shortcuts import render
from django.http.response import FileResponse
from django.shortcuts import loader

def index(request):
    return render(request,
                  'staticstuff/index.html')

def about(request):
    return render(request,
                  'staticstuff/about.html')

def showcase(request):
    return render(request,
                  'staticstuff/showcase.html')
