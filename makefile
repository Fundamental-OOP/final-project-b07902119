run:
	cd FOOP_final/TowerDefense && \
	mvn javafx:run

update:
	git submodule foreach --recursive git pull origin main
	git add FOOP_final
	git commit -m "update submodule"
	git push
