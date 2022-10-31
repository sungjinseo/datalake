FROM public.ecr.aws/j1r0q0g6/notebooks/notebook-servers/jupyter-tensorflow:master-e9324d39

WORKDIR /home/jovyan

USER root

# 카카오 ubuntu archive mirror server 추가. 다운로드 속도 향상
RUN sed -i 's@archive.ubuntu.com@mirror.kakao.com@g' /etc/apt/sources.list && \
    apt-get update

RUN apt-get update && apt-get install -yq --no-install-recommends \
  ca-certificates \
  curl \
  gcc \
  g++ \
  git \
  openssh-client \
  sudo \
  unzip \
  vim \
  wget \
  zip \
  build-essential \
  python3-setuptools \  
  && apt-get clean && \
  rm -rf /var/lib/apt/lists/*

RUN pip install --upgrade pip

# kubeflow packages
RUN pip install kfp==1.6.3 && \
    pip install kfp-server-api==1.6.0 && \
    pip install kfserving==0.5.1 && \
    pip install kubeflow-fairing

# common packages
# numpy 1.19.5로 설치해야함
RUN pip install bokeh==2.3.2 && \
    pip install cloudpickle==1.6.0 && \
	pip install dill==0.3.4 && \
	pip install ipympl==0.7.0 && \
	pip install ipywidgets==7.6.3 && \
	pip install jupyterlab-git==0.30.1 && \
	pip install matplotlib==3.4.2 && \
	pip install pandas==1.2.4 && \
	pip install scikit-image==0.18.1 && \
	pip install scikit-learn==0.24.2 && \
	pip install scipy==1.7.0 && \
	pip install seaborn==0.11.1 && \
	pip install xgboost==1.4.2 && \

# tensorflow packages
RUN pip install keras==2.4.3

RUN apt update
RUN apt-get install -y fontconfig

RUN apt-get install fonts-nanum* && \
    mkdir ~/.local && \
	mkdir ~/.local/share && \
	mkdir ~/.local/share/fonts && \
    cd ~/.local/share/fonts && \
    wget https://github.com/naver/d2codingfont/releases/download/VER1.3.2/D2Coding-Ver1.3.2-20180524.zip && \
    unzip D2Coding-Ver1.3.2-20180524.zip && \
	mkdir /usr/share/fonts/truetype/D2Coding && \
    cp ./D2Coding/*.ttf /usr/share/fonts/truetype/D2Coding/ && \
    fc-cache -fv && \
    rm -rf D2Coding* && \
    rm -rf ~/.cache/matplotlib/*

# konlpy, py-hanspell, soynlp 패키지 설치 
RUN pip install konlpy && \
    pip install git+https://github.com/ssut/py-hanspell.git && \
    pip install soynlp && \
    pip install soyspacing && \
    pip install krwordrank && \
    pip install soykeyword

# 형태소 분석기 mecab 설치
RUN cd /tmp && \
    wget "https://www.dropbox.com/s/9xls0tgtf3edgns/mecab-0.996-ko-0.9.2.tar.gz?dl=1" && \
    tar zxfv mecab-0.996-ko-0.9.2.tar.gz?dl=1 && \
    cd mecab-0.996-ko-0.9.2 && \
    ./configure && \
    make && \
    make check && \
    make install && \
    ldconfig

RUN cd /tmp && \
    wget "https://www.dropbox.com/s/i8girnk5p80076c/mecab-ko-dic-2.1.1-20180720.tar.gz?dl=1" && \
    apt install -y autoconf && \
    tar zxfv mecab-ko-dic-2.1.1-20180720.tar.gz?dl=1 && \
    cd mecab-ko-dic-2.1.1-20180720 && \
    ./autogen.sh && \
    ./configure && \
    make && \
    make install && \
    ldconfig

# 형태소 분석기 mecab 파이썬 패키지 설치
RUN cd /tmp && \
    git clone https://bitbucket.org/eunjeon/mecab-python-0.996.git && \
    cd mecab-python-0.996 && \
    python setup.py build && \
    python setup.py install

# locale 설정
RUN apt-get update && apt-get install -y vim locales && \
    locale-gen ko_KR.UTF-8 && locale -a && \
    ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

# LANG 환경변수 설정
ENV LANG ko_KR.UTF-8