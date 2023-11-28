from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
import random

app = FastAPI()

origins = [
    'http://localhost',
    'http://localhost:3000',
]

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=['*'],
    allow_headers=['*']
)

@app.get('/api/example')
async def root():
    return 'Hello World'


teachers = [
    {'name':  'Andrey Vitalievich'},
    {'name':  'Vitaliy Vitalievich'},
    {'name':  'Andrey Andreevich'},
    {'name':  'Vitaliy Andreevich'},
    {'name':  'Рулон Обоев'},
    {'name':  'Улов Кальмаров'},
    {'name':  'Угон Камазов'},
    {'name':  'Удар Морозов'}
]
for i, tea in enumerate(teachers):
    tea.update({'id': i})

@app.get('/teacher')
async def getAllTeachers():
    return teachers


audiences = [
    {'name': '123'},
    {'name': '124'},
    {'name': '125'},
    {'name': '126'},
    {'name': '127'},
    {'name': '128'},
    {'name': '1155'},
    {'name': '105арбуз'},    
]
for i, aud in enumerate(audiences):
    aud.update({'id': i})

@app.get('/audience')
async def getAllAudiences():
    return audiences




subjects = [
    {'name': 'философия'},
    {'name': 'уппрпо'},
    {'name': 'ооаид'},
    {'name': 'ооо'},
    {'name': 'опп'},
    {'name': 'ооп'},
    {'name': 'философия'},
    {'name': 'право'},   
]
for i, subj in enumerate(subjects):
    subj.update({'id': i})

@app.get('/subject')
async def getAllSubjects():
    return subjects




groups = [
    {'name': '21214'},
    {'name': '21215'},
    {'name': '21216'},
    {'name': '21217'},
    {'name': '222'},
    {'name': '333'},
    {'name': 'УП111'},
    {'name': 'ФВ123123123'},   
]
for i, grp in enumerate(groups):
    grp.update({'id': i})

@app.get('/group')
async def getAllSubjects():
    return groups





# table

import random
def generateTable(num):
    table = {
        'days': 6,
        'lessonsPerDay': 6,
        'cells': []
    }

    for i in range(num):
        table['cells'].append(
            {
                'day': random.randint(0, 100500) % table['days'],
                'lesson': random.randint(0, 100500) % table['lessonsPerDay'],
                'teacher': teachers[random.randrange(len(teachers))],
                'audience': audiences[random.randrange(len(audiences))],
                'group': groups[random.randrange(len(subjects))],
                'subject': subjects[random.randrange(len(groups))]
            }
        )

    return table

#init
table = generateTable(100)

@app.post('/table')
async def regenerateTable():
    table = generateTable(100)
    return {'ok': 'kok'}

@app.post('/table/{num}')
async def regenerateTable(num: int):
    table = generateTable(num)
    return {'ok': 'kok'}

@app.get('/table')
async def getTable():
    return {'table': table}

@app.get('/table/by-audience/{name}')
async def getTableByGroupId(name: str):
    newTable = table.copy()
    newTable['cells'] = list(filter((lambda x : x['audience']['name'] == name), table['cells']))

    return newTable

@app.get('/table/by-group/{name}')
async def getTableByGroupId(name: str):
    newTable = table.copy()
    newTable['cells'] = list(filter((lambda x : x['group']['name'] == name), table['cells']))

    return newTable


@app.get('/table/by-teacher/{name}')
async def getTableByTeacherId(name: str):
    newTable = table.copy()
    newTable['cells'] = list(filter((lambda x : x['teacher']['name'] == name), table['cells']))

    return newTable

