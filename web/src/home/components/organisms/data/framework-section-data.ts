import {
  getApiDropwizardCloudMetricsLive,
  getApiQuarkusCloudMetricsLive,
  getApiSpringCloudMetricsLive,
} from "@/api/client"
import {
  getApiDropwizardCloudMetricsInfoOptions,
  getApiQuarkusCloudMetricsInfoOptions,
  getApiSpringCloudMetricsInfoOptions,
} from "@/api/client/@tanstack/react-query.gen"

// Data
export type Framework = "dropwizard" | "quarkus" | "spring"

export type FrameworkSection = Record<Framework, object>

export const frameworkSectionData = {
  dropwizard: {
    title: "Dropwizard",
    serviceInformationQueryOptions: getApiDropwizardCloudMetricsInfoOptions,
    getLiveMetricsSse: getApiDropwizardCloudMetricsLive,
  },
  quarkus: {
    title: "Quarkus",
    serviceInformationQueryOptions: getApiQuarkusCloudMetricsInfoOptions,
    getLiveMetricsSse: getApiQuarkusCloudMetricsLive,
  },
  spring: {
    title: "Spring",
    serviceInformationQueryOptions: getApiSpringCloudMetricsInfoOptions,
    getLiveMetricsSse: getApiSpringCloudMetricsLive,
  },
} satisfies FrameworkSection
