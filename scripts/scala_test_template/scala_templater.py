from jinja2 import Template
from jinja2 import FileSystemLoader
from jinja2 import Environment

def load_template(file_name):
    templateLoader = FileSystemLoader(searchpath='./scala_test_template.scala')
    templateEnv = Environment(loader=templateLoader)
    template = templateEnv.get_template(file_name)
    return template.render(module_name="Amirali", in_args=[2,4,5], return_args=[32])


if __name__ == '__main__':
    f = open('scala_render_template.scala', 'w+')
    f.write(load_template('scala_test_template.scala'))
