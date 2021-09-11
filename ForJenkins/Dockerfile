FROM jenkins/jenkins:lts

USER root

COPY docker_install.sh /docker_install.sh
RUN chmod +x /docker_install.sh
RUN /docker_install.sh

RUN groupadd -f docker
RUN usermod -aG docker jenkins
USER jenkins