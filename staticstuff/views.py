from django.shortcuts import render

def index(request):
    return render(request,
                  'staticstuff/index.html')

def about(request):
    return render(request,
                  'staticstuff/index.html')

def showcase(request):
    return render(request,
                  'staticstuff/index.html')

def cv(request):
    return render(request,
                  'staticstuff/index.html')
